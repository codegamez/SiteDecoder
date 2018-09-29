import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import java.net.UnknownHostException
import java.util.regex.Pattern

private var searchText: String? = null
private var page = 1
private var choose: Int? = null
private var isHelp = false

private val videoFormats = HashMap<String, String>().apply {
    put(".zip", ".zip")
    put(".rar", ".rar")
}

fun main(args: Array<String>) {

    args.forEachIndexed { index, text ->

        when(text) {

            "search", "-s" -> {
                searchText = args.getOrNull(index+1)
            }

            "-p", "--page" -> {
                page = args.getOrNull(index+1)?.toIntOrNull() ?: 1
            }

            "-c", "--choose" -> {
                choose = args.getOrNull(index+1)?.toIntOrNull()
            }

            "-h", "--help" -> {
                isHelp = true
            }

        }

    }

    if(isHelp) {

        print("""

            Move Site Decoder

            (search, -s) - get a string for search in site
            (-p, --page) - get page number
            (-c, --choose) - get a number for selected post
            (-h, --help) - this text you see now

        """)

        return
    }

    val url = "http://worldsubtitle.us/page/$page/?${if(searchText.isNullOrBlank()) "" else "s=$searchText"}"

    try {

        val doc = Jsoup.connect(url).get()

        val articleList = doc.select(".cat-post")

        if(choose != null) {

            val element = articleList[choose!!-1]

            val titleEl = element.select(".cat-post-titel h2 a")[0]
            val cUrl = titleEl.attr("href")

            System.out.print(ConsoleColors.BLUE_BOLD)
            println("\n$cUrl\n")
            System.out.print(ConsoleColors.RESET)

            val cDoc = Jsoup.connect(cUrl).get()

            val linkList = cDoc.select(".single-dl a")

            linkList.forEachIndexed { index, linkEl ->

                val link = linkEl.attr("href")

                if(videoFormats.containsKey(link.getFormat())) {

                    val name = linkEl.text().enNum().fa2en().rmFa()

                    System.out.print(ConsoleColors.WHITE_BOLD)
                    println(if(name.isBlank())
                        link.getFileName()
                    else
                        name)

                    System.out.print(ConsoleColors.BLUE)
                    println(link)

                    println()
                }

            }
            System.out.print(ConsoleColors.RESET)

        }else {

            System.out.print(ConsoleColors.BLUE_BOLD)
            println("\n$url\n")
            System.out.print(ConsoleColors.RESET)

            articleList.forEachIndexed { index, element ->
                val titleEl = element.select(".cat-post-titel h2 a")[0]
                println("${index+1}. ${titleEl.text().enNum().fa2en().rmFa()}")
            }

        }

    }catch (hse: HttpStatusException) {
        when(hse.statusCode) {
            404 -> {
                println("Not Found")
            }
        }
    }catch (uhe: UnknownHostException) {
        println("Connection Error")
    }

}

fun String.getFormat(): String {
    val index = lastIndexOf('.')
    return if(index < 0)
        ""
    else {
        substring(index, length)
    }
}

fun String.enNum(): String {
    val s = toCharArray()
    s.forEachIndexed { index, c ->
        when(c) {
            '۱' -> s[index] = '1'
            '۲' -> s[index] = '2'
            '۳' -> s[index] = '3'
            '۴' -> s[index] = '4'
            '۵' -> s[index] = '5'
            '۶' -> s[index] = '6'
            '۷' -> s[index] = '7'
            '۸' -> s[index] = '8'
            '۹' -> s[index] = '9'
            '۰' -> s[index] = '0'
        }
    }
    return String(s)
}

fun String.fa2en(): String {
    var s = this

    s = s.replace("دانلود", "Download")
    s = s.replace("سرور", "server")
    s = s.replace("مستقیم", "direct")
    s = s.replace("فیلم های", "movies")
    s = s.replace("فیلم ها", "movies")
    s = s.replace("فیلم", "movie")
    s = s.replace("انیمیشن های", "animations")
    s = s.replace("انیمیشن ها", "animations")
    s = s.replace("انیمیشن", "animation")
    s = s.replace("دوبله", "dubbed")
    s = s.replace("فارسی", "persian")
    s = s.replace("مجموعه", "Collection")
    s = s.replace("کالکشن", "Collection")
    s = s.replace("از", "from")
    s = s.replace("سریال های", "series")
    s = s.replace("سریال ها", "series")
    s = s.replace("سریال", "series")
    s = s.replace("نسخه", "version")
    s = s.replace("لینک", "link")
    s = s.replace("لینک", "link")
    s = s.replace("تریلر", "trailer")

    return s
}

fun String.rmFa(): String {
    val s = toCharArray()
    val b = StringBuilder()

    s.forEach {
        if(it.toInt() !in 1570..1740) {
            b.append(it)
        }
    }

    return b.toString().trim().replace(Regex("\\s+"), " ")
}

fun String.getFileName(): String {
    val index = lastIndexOf('/')
    return if(index < 0)
        ""
    else {
        substring(index+1, length).replace(getFormat(), "")
    }
}