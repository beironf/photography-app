package backend.photography.repositories.model.photo

import backend.photography.entities.photo.meta.Metadata
import backend.photography.entities.photo.settings.CameraSettings

import java.sql.Timestamp

case class UpdatePhotoDb(title: String,
                         description: Option[String],
                         photographer: String,
                         group: Option[String],
                         taken: Timestamp,
                         camera: String,
                         lens: String,
                         cameraSettings: CameraSettings,
                         metadata: Metadata)