package backend.photo.api.model

import backend.photo.api.model.dtos._
import backend.photo.entities.{Photo, UpdatePhoto}
import backend.photo.entities.gear.Gear
import backend.photo.entities.meta.Metadata

trait ImplicitDtoConversion extends ImplicitEnumConversion {

  implicit class PhotoToDto(photo: Photo) {
    def toDto: PhotoDto = PhotoDto(
      photo.imageId,
      photo.title,
      photo.description,
      photo.photographer,
      photo.group,
      photo.location,
      photo.taken,
      photo.gear.toDto,
      photo.cameraSettings,
      photo.metadata.toDto,
      photo.judgement
    )

    def toDtoWithRatio(width: Int, height: Int): PhotoWithRatioDto = PhotoWithRatioDto(photo.toDto, width, height)
  }

  implicit class PhotoDtoToDomain(photoDto: PhotoDto) {
    def toDomain: Photo = Photo(
      photoDto.imageId,
      photoDto.title,
      photoDto.description,
      photoDto.photographer,
      photoDto.group,
      photoDto.location,
      photoDto.taken,
      photoDto.gear.toDomain,
      photoDto.cameraSettings,
      photoDto.metadata.toDomain,
      photoDto.judgement
    )
  }

  implicit class UpdatePhotoDtoToDomain(updateDto: UpdatePhotoDto) {
    def toDomain: UpdatePhoto = UpdatePhoto(
      updateDto.title,
      updateDto.description,
      updateDto.photographer,
      updateDto.group,
      updateDto.location,
      updateDto.taken,
      updateDto.gear.toDomain,
      updateDto.cameraSettings,
      updateDto.metadata.toDomain,
      updateDto.judgement
    )
  }

  implicit class GearToDto(gear: Gear) {
    def toDto: GearDto = GearDto(
      gear.camera.toDto,
      gear.lens.toDto
    )
  }

  implicit class GearDtoToDomain(gearDto: GearDto) {
    def toDomain: Gear = Gear(
      gearDto.camera.toDomain,
      gearDto.lens.toDomain
    )
  }

  implicit class MetadataToDto(metadata: Metadata) {
    def toDto: MetadataDto = MetadataDto(
      metadata.category.toDto,
      metadata.cameraTechniques.map(_.toDto),
      metadata.tags
    )
  }

  implicit class MetadataDtoToDomain(metadataDto: MetadataDto) {
    def toDomain: Metadata = Metadata(
      metadataDto.category.toDomain,
      metadataDto.cameraTechniques.map(_.toDomain),
      metadataDto.tags
    )
  }

}
