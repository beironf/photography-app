import React from 'react';
import { TileLayer, MapContainer } from 'react-leaflet';
import { LatLngExpression } from 'leaflet';
import { LocationMarker } from './LocationMarker';

type Props = {
  coordinates?: number[]; // [lat, long]
  setCoordinates: (_: number[]) => void;
  height?: number;
};

export const MapInput: React.FunctionComponent<Props> = ({
  coordinates, setCoordinates, height,
}) => (
  <MapContainer
    style={{ height: height ?? 350 }}
    center={[50, 10]}
    zoom={3}
  >
    <TileLayer
      attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
      url="http://{s}.tile.osm.org/{z}/{x}/{y}.png"
    />
    <LocationMarker
      location={coordinates ? coordinates as LatLngExpression : undefined}
      setLocation={(latLng) => setCoordinates([latLng.lat, latLng.lng])}
    />
  </MapContainer>
);
