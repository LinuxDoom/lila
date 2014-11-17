package lila.common

import play.api.http.HeaderNames
import play.api.mvc.RequestHeader

object HTTPRequest {

  def isXhr(req: RequestHeader): Boolean =
    (req.headers get "X-Requested-With") == Some("XMLHttpRequest")

  def isSocket(req: RequestHeader): Boolean =
    (req.headers get HeaderNames.UPGRADE) == Some("websocket")

  def isSynchronousHttp(req: RequestHeader) = !isXhr(req) && !isSocket(req)

  def isSafe(req: RequestHeader) = req.method == "GET"

  def isRedirectable(req: RequestHeader) = isSynchronousHttp(req) && isSafe(req)

  def fullUrl(req: RequestHeader): String = "http://" + req.host + req.uri

  def userAgent(req: RequestHeader): Option[String] = req.headers get HeaderNames.USER_AGENT

  def sid(req: RequestHeader): Option[String] = req.session get "sid"

  private val isBotPattern =
    """.*(googlebot|Googlebot-Mobile|Googlebot-Image|Mediapartners-Google|bingbot|slurp|java|wget|curl|Commons-HttpClient|Python-urllib|libwww|httpunit|nutch|phpcrawl|msnbot|Adidxbot|blekkobot|teoma|ia_archiver|GingerCrawler|webmon|httrack|webcrawler|FAST-WebCrawler|FASTEnterpriseCrawler|convera|biglotron|grub.org|UsineNouvelleCrawler|antibot|netresearchserver|speedy|fluffy|jyxobot|bibnum.bnf|findlink|exabot|gigabot|msrbot|seekbot|ngbot|panscient|yacybot|AISearchBot|IOI|ips-agent|tagoobot|MJ12bot|dotbot|woriobot|yanga|buzzbot|mlbot|yandex|purebot|LingueeBot|Voyager|CyberPatrol|voilabot|baiduspider|citeseerxbot|spbot|twengabot|postrank|turnitinbot|scribdbot|page2rss|sitebot|linkdex|ezooms|dotbot|mail.ru|discobot|heritrix|findthatfile|europarchive.org|NerdByNature.Bot|sistrixcrawler|ahrefsbot|Aboundex|domaincrawler|wbsearchbot|summify|ccbot|edisterbot|seznambot|ec2linkfinder|gslfbot|aihitbot|intelium_bot|facebookexternalhit|yeti|RetrevoPageAnalyzer|lb-spider|sogou|lssbot|careerbot|wotbox|wocbot|ichiro|DuckDuckBot|lssrocketcrawler|drupact|webcompanycrawler|acoonbot|openindexspider|gnamgnamspider|web-archive-net.com.bot|backlinkcrawler|coccoc|integromedb|contentcrawlerspider|toplistbot|seokicks-robot|it2media-domain-crawler|ip-web-crawler.com|siteexplorer.info|elisabot|proximic|changedetection|blexbot|arabot|WeSEE:Search|niki-bot|CrystalSemanticsBot|rogerbot|360Spider|psbot|InterfaxScanBot|LipperheySEOService|CCMetadataScaper|g00g1e.net|GrapeshotCrawler|urlappendbot|brainobot|fr-crawler|binlar|SimpleCrawler|SimpleCrawler|Livelapbot|Twitterbot|cXensebot|smtbot).*""".r.pattern

  def isBot(req: RequestHeader): Boolean = userAgent(req) ?? { ua =>
    isBotPattern.matcher(ua).matches
  }
}
