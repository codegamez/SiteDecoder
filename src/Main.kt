import com.google.gson.Gson
import model.SiteModel
import model.get
import java.io.File
import java.io.FileReader

private var isHelp = false
private var isData = false
private val decoder = SiteDecoder()
private val siteList = getSiteData()

fun main(args: Array<String>) {

    args.forEachIndexed { index, text ->

        if(index == 0 && !text.startsWith('-')) {
            decoder.targetSite = siteList.get(text)
            return@forEachIndexed
        }

        when(text) {

            "search", "-s" -> {

                var i = index + 1
                decoder.searchText = ""
                while (args.getOrNull(i)?.startsWith('-') == false) {
                    decoder.searchText += args.getOrNull(i++) + "+"
                }
                decoder.searchText?.trim('+')
            }

            "-p", "--page" -> {
                decoder.pageNumber = args.getOrNull(index+1)?.toIntOrNull() ?: 1
            }

            "-c", "--choose" -> {
                decoder.chooseNumber = args.getOrNull(index+1)?.toIntOrNull()
            }

            "-h", "--help" -> {
                isHelp = true
                return@forEachIndexed
            }

            "--data" -> {
                isData = true
                return@forEachIndexed
            }

        }

    }

    when {
        isHelp -> println("""
        Site Decoder

        (search, -s) - get a string as an argument for search in site
        (-p, --page) - get page number as an argument
        (-c, --choose) - get a number as an argument for selected post
        (-h, --help) - this text you see now
        (--data) - path of data.json file
    """)
        isData -> {
            print(ConsoleColors.BLUE)
            println(dataPath)
        }
        else -> decoder.decode()
    }

}

private lateinit var dataPath:String
fun getSiteData(): Array<SiteModel> {
    val path = SiteDecoder::class.java.classLoader.getResource("asset/data.json").path
    val file = File(path)
    val reader = FileReader(file)
    val dataJson = reader.readText()
    dataPath = file.absolutePath
    reader.close()

    return Gson().fromJson<Array<SiteModel>>(dataJson, Array<SiteModel>::class.java)
}