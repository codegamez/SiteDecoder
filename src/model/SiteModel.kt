package model

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

    fun getDomain(): String {
        var s = url.trim('/', ' ')
        s = s.replaceFirst("http://", "")
        s = s.replaceFirst("https://", "")
        return s.trim('/', ' ')
    }

    fun getUrl(): String {
        var s = url.trim('/', ' ')
        if(!s.startsWith("http://") && !s.startsWith("https://"))
            s = "http://" + s
        return s
    }

    fun isValidFormat(f: String): Boolean {
        return format.contains(f)
    }

    fun getSearchUrl(): String {
        val s = searchUrl.trim('/', ' ')
        return "/$s"
    }

    fun getPostSelector(): String? {
        return postSelector
    }

    fun getPostTitleLinkSelector(): String {
        return postTitleLinkSelector
    }

    fun getPostDownloadLinkSelector(): String? {
        return postDownloadLinkSelector
    }

}

fun Array<SiteModel>.get(name: String): SiteModel? {
    return find { it.getName() == name }
}