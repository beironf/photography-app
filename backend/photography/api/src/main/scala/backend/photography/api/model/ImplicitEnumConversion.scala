package backend.photography.api.model

import backend.photography.api.model.enums.*
import backend.photography.api.model.enums.CameraDto.*
import backend.photography.api.model.enums.CameraTechniqueDto.*
import backend.photography.api.model.enums.CategoryDto.*
import backend.photography.api.model.enums.LensDto.*
import backend.photography.entities.photo.gear.Camera.Camera
import backend.photography.entities.photo.gear.Lens.Lens
import backend.photography.entities.photo.gear.{Camera, Lens}
import backend.photography.entities.photo.meta.CameraTechnique.CameraTechnique
import backend.photography.entities.photo.meta.Category.Category
import backend.photography.entities.photo.meta.{CameraTechnique, Category}

trait ImplicitEnumConversion {

  implicit class CameraDtoToDomain(c: CameraDto) {
    def toDomain: Camera = c match {
      case CanonEOS600D => Camera.CanonEOS600D
      case CanonEOS5DMarkII => Camera.CanonEOS5DMarkII
      case FujifilmX100F => Camera.FujifilmX100F
      case FujifilmXT4 => Camera.FujifilmXT4
      case DJIMini3 => Camera.DJIMini3
    }
  }

  implicit class CameraToDto(c: Camera) {
    def toDto: CameraDto = c match {
      case Camera.CanonEOS600D => CanonEOS600D
      case Camera.CanonEOS5DMarkII => CanonEOS5DMarkII
      case Camera.FujifilmX100F => FujifilmX100F
      case Camera.FujifilmXT4 => FujifilmXT4
      case Camera.DJIMini3 => DJIMini3
    }
  }

  implicit class LensDtoToDomain(c: LensDto) {
    def toDomain: Lens = c match {
      case Sigma10_20 => Lens.Sigma10_20
      case Canon50F1_8 => Lens.Canon50F1_8
      case Canon70_300 => Lens.Canon70_300
      case Canon24_70L => Lens.Canon24_70L
      case Fujinon35 => Lens.Fujinon35
      case Fujinon16 => Lens.Fujinon16
      case Fujinon90 => Lens.Fujinon90
      case DJI6_7F1_7 => Lens.DJI6_7F1_7
    }
  }

  implicit class LensToDto(c: Lens) {
    def toDto: LensDto = c match {
      case Lens.Sigma10_20 => Sigma10_20
      case Lens.Canon50F1_8 => Canon50F1_8
      case Lens.Canon70_300 => Canon70_300
      case Lens.Canon24_70L => Canon24_70L
      case Lens.Fujinon35 => Fujinon35
      case Lens.Fujinon16 => Fujinon16
      case Lens.Fujinon90 => Fujinon90
      case Lens.DJI6_7F1_7 => DJI6_7F1_7
    }
  }

  implicit class CameraTechniqueDtoToDomain(c: CameraTechniqueDto) {
    def toDomain: CameraTechnique = c match {
      case LongExposure => CameraTechnique.LongExposure
      case Panorama => CameraTechnique.Panorama
      case Aerial => CameraTechnique.Aerial
      case Macro => CameraTechnique.Macro
      case Zooming => CameraTechnique.Zooming
      case Filters => CameraTechnique.Filters
      case MultipleFocusPoints => CameraTechnique.MultipleFocusPoints
    }
  }

  implicit class CameraTechniqueToDto(c: CameraTechnique) {
    def toDto: CameraTechniqueDto = c match {
      case CameraTechnique.LongExposure => LongExposure
      case CameraTechnique.Panorama => Panorama
      case CameraTechnique.Aerial => Aerial
      case CameraTechnique.Macro => Macro
      case CameraTechnique.Zooming => Zooming
      case CameraTechnique.Filters => Filters
      case CameraTechnique.MultipleFocusPoints => MultipleFocusPoints
    }
  }

  implicit class CategoryDtoToDomain(c: CategoryDto) {
    def toDomain: Category = c match {
      case Abstract => Category.Abstract
      case Animal => Category.Animal
      case CityAndArchitecture => Category.CityAndArchitecture
      case Landscape => Category.Landscape
      case Nature => Category.Nature
      case Night => Category.Night
      case People => Category.People
    }
  }

  implicit class CategoryToDto(c: Category) {
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

}
