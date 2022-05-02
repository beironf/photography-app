package backend.photo.api.model.dtos

import backend.photo.api.model.enums.{CameraDto, LensDto}

case class GearDto(camera: CameraDto,
                   lens: LensDto)
