import model.SiteModel
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import java.net.UnknownHostException

class SiteDecoder(var targetSite: SiteModel? = null, var searchText: String? = null, var pageNumber: Int = 1, var chooseNumber: Int? = null) {

    fun decode() {

        if(targetSite == null) {
            print(ConsoleColors.RED)
            println("This site does not exist!")
            return
        }

        var url = targetSite!!.getSearchUrl()
        url = url.replace("%s", searchText ?: "")
        url = url.replace("%p", "$pageNumber")

        url = targetSite!!.getUrl() + url

        try {

            val doc = Jsoup.connect(url).get()

            val articleList = doc.select(targetSite!!.getPostSelector() ?: targetSite!!.getPostTitleLinkSelector())

            if(chooseNumber != null) {

                val element = articleList[chooseNumber!!-1]

                val titleEl = if(targetSite!!.getPostSelector() == null)
                    element
                else
                    element.select(targetSite!!.getPostTitleLinkSelector())[0]

                val cUrl = titleEl.attr("href")

                System.out.print(ConsoleColors.BLUE_BOLD)
                println("\n$cUrl\n")
                System.out.print(ConsoleColors.RESET)

                val cDoc = Jsoup.connect(cUrl).get()

                val linkList = cDoc.select(targetSite!!.getPostDownloadLinkSelector() ?: "a")

                linkList.forEachIndexed { index, linkEl ->

                    val link = linkEl.attr("href")

                    if(targetSite!!.isValidFormat(link.getFormat())) {

                        System.out.print(ConsoleColors.WHITE_BOLD)
                        println(link.getFileName())

                        System.out.print(ConsoleColors.BLUE)
                        println(link)

                        println()
                    }

                }
                print(ConsoleColors.RESET)

            }else {

                print(ConsoleColors.BLUE_BOLD)
                println("\n$url\n")
                print(ConsoleColors.RESET)

                articleList.forEachIndexed { index, element ->
                    val titleEl = if(targetSite!!.getPostSelector() == null)
                        element
                    else
                        element.select(targetSite!!.getPostTitleLinkSelector())[0]

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

}