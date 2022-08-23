package app.thebitsolutions.filedownloader

interface OnDownloadProgressListener {
    fun percent(percent: Int)
    fun downloadStart()
    fun downloadedSuccess()
    fun downloadFail(error: String?)
    fun downloadCancel()
}