lazy val runPhotoApi = taskKey[Unit]("Run Photo API")
val mainClassName = "backend.photo.api.PhotoApi"

runPhotoApi := {
  (Compile / runner).value.run(
    mainClass = mainClassName,
    classpath = (Runtime / fullClasspath).value.files,
    options = Array(""),
    log = streams.value.log
  )
}