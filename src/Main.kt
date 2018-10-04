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
                decoder.searchText = args.getOrNull(index+1)
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

    if(isHelp) {
        println("""
    Move Site Decoder

    (search, -s) - get a string for search in site
    (-p, --page) - get page number
    (-c, --choose) - get a number for selected post
    (-h, --help) - this text you see now
        """)
    }else if(isData) {
        println("Data File Path:")
        print(ConsoleColors.BLUE)
        println(dataPath)
    }else {
        decoder.decode()
    }

}

private lateinit var dataPath:String
fun getSiteData(): Array<SiteModel> {
    val path = getBaseUrl() + "/asset/data.json"
    val file = File(path)
    val reader = FileReader(file)
    val dataJson = reader.readText()
    dataPath = file.absolutePath
    reader.close()

    return Gson().fromJson<Array<SiteModel>>(dataJson, Array<SiteModel>::class.java)
}