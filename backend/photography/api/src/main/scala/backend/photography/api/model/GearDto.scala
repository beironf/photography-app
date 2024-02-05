package backend.photography.api.model

import backend.photography.api.model.enums.{CameraDto, LensDto}

case class GearDto(camera: CameraDto,
                   lens: LensDto)
