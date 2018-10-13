package model

@Suppress("ArrayInDataClass")
data class SiteModel(
        private val name: String,
        private val url: String,
        private val format: Array<String>,
        private val searchUrl: String,
        private val postSelector: String?,
        private val postTitleLinkSelector: String,
        private val postDownloadLinkSelector: String?) {

    fun getName(): String {
        return name
    }

    // return site address like (example.com)
    fun getDomain(): String {
        var s = url.trim('/', ' ')
        s = s.replaceFirst("http://", "")
        s = s.replaceFirst("https://", "")
        return s.trim('/', ' ')
    }

    // return site address like (http://example.com)
    fun getUrl(): String {
        var s = url.trim('/', ' ')
        if(!s.startsWith("http://") && !s.startsWith("https://"))
            s = "http://$s"
        return s
    }

    // check if format is in requested format list
    fun isValidFormat(f: String): Boolean {
        return format.contains(f)
    }

    // return formated search url like (/?s=%s&p=%p)
    fun getSearchUrl(): String {
        val s = searchUrl.trim('/', ' ')
        return "/$s"
    }

    // return css selector for repeated post container
    fun getPostSelector(): String? {
        return postSelector
    }

    // return css selector for repeated post title that is inside post container - most be <a> tag
    fun getPostTitleLinkSelector(): String {
        return postTitleLinkSelector
    }

    // return css selector for an area to search for download links - most be <a> tag
    fun getPostDownloadLinkSelector(): String? {
        return postDownloadLinkSelector
    }

}

fun Array<SiteModel>.get(name: String): SiteModel? {
    return find { it.getName() == name }
}