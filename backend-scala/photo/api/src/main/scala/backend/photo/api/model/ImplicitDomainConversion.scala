package backend.photo.api.model

import backend.photo.api.model.CategoryDto._
import backend.photo.api.model.request.AddPhoto
import backend.photo.api.model.response.PhotoDto
import backend.photo.entities.Photo
import backend.photo.entities.meta.Category

trait ImplicitDomainConversion {

  implicit class CategoryDtoToDomain(c: CategoryDto) {
    def toDomain: Category.Value = c match {
      case Abstract => Category.Abstract
      case Animal => Category.Animal
      case CityAndArchitecture => Category.CityAndArchitecture
      case Landscape => Category.Landscape
      case Nature => Category.Nature
      case Night => Category.Night
      case People => Category.People
    }
  }

  implicit class CategoryToDto(c: Category.Value) {
    def toDto: CategoryDto = c match {
      case Category.Abstract => Abstract
      case Category.Animal => Animal
      case Category.CityAndArchitecture => CityAndArchitecture
      case Category.Landscape => Landscape
      case Category.Nature => Nature
      case Category.Night => Night
      case Category.People => People
    }
  }

  implicit class PhotoToDto(photo: Photo) {
    def toDto: PhotoDto = PhotoDto(
      photo.imageId,
      photo.title,
      photo.description,
      photo.photographer,
      photo.group,
      photo.location,
      photo.taken,
      photo.gear,
      photo.cameraSettings,
      photo.metadata,
      photo.judgement
    )
  }

  implicit class AddPhotoToDomain(addPhoto: AddPhoto) {
    def toDomain: Photo = Photo(
      addPhoto.imageId,
      addPhoto.title,
      addPhoto.description,
      addPhoto.photographer,
      addPhoto.group,
      addPhoto.location,
      addPhoto.taken,
      addPhoto.gear,
      addPhoto.cameraSettings,
      addPhoto.metadata,
      addPhoto.judgement
    )
  }

}
