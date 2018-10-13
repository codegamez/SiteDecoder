
// Example Data File (it must be json array)

[
    {

        "name" : "subtitle", // Name -required

        "url" : "http://worldsubtitle.us", // Site Address -required

        "format" : ["zip", "rar"], // Downloadable File Formats -required

        "searchUrl" : "/page/%p/?s=%s", // Path Of Search Part Of Site Without The Site Address", %s is for search text -required

        "postSelector" : ".cat-post", // Css Selector For Site Posts

        "postTitleLinkSelector" : ".cat-post-titel h2 a", // Css Selector For Title Of Posts. It Must Be A Link <a> -required

        "postDownloadLinkSelector" : ".single-dl a" // Css Selector For Link Of Downloadable Files. It Must Be A Link <a>

    }
]
