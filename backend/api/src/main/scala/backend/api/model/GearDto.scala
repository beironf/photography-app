package backend.api.model

import backend.api.model.enums.{CameraDto, LensDto}

case class GearDto(camera: CameraDto,
                   lens: LensDto)
