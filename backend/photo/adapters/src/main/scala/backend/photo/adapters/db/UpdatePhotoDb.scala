package backend.photo.adapters.db

import backend.photo.entities.meta.Metadata
import backend.photo.entities.settings.CameraSettings

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