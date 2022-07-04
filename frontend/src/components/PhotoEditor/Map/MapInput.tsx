import React, { useEffect } from 'react';
import {
  TileLayer, MapContainer, useMap,
} from 'react-leaflet';
import { LatLngExpression } from 'leaflet';
import { theme } from 'style/theme';
import { LocationMarker } from './LocationMarker';

type Props = {
  coordinates?: number[]; // [lat, long]
  setCoordinates: (_: number[]) => void;
  height?: number;
};

const MapUseEffect: React.FunctionComponent<{coordinates?: number[]}> = ({ coordinates }) => {
  const map = useMap();

  useEffect(() => {
    if (coordinates !== undefined) {
      const zoom = map.getZoom();
      map.flyTo(coordinates as LatLngExpression, zoom < 10 ? 10 : zoom);
    }
  }, [coordinates, map]);

  return null;
};

export const MapInput: React.FunctionComponent<Props> = ({
  coordinates, setCoordinates, height,
}) => (
  <MapContainer
    style={{
      height: height ?? 370,
      margin: `${-theme.primaryPadding}px ${-theme.primaryPadding}px ${theme.primaryPadding}px`,
    }}
    center={coordinates !== undefined ? coordinates as LatLngExpression : [50, 10]}
    zoom={coordinates !== undefined ? 10 : 3}
    zoomControl={false}
    maxZoom={17}
    minZoom={2}
  >
    <TileLayer
      attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
      url="http://{s}.tile.osm.org/{z}/{x}/{y}.png"
    />
    <LocationMarker
      location={coordinates ? coordinates as LatLngExpression : undefined}
      setLocation={(latLng) => setCoordinates([latLng.lat, latLng.lng])}
    />
    <MapUseEffect coordinates={coordinates} />
  </MapContainer>
);
