import React from 'react';
import {
  useMapEvent, Marker,
} from 'react-leaflet';
import { LatLng, LatLngExpression, LeafletMouseEvent } from 'leaflet';

type Props = {
  location?: LatLngExpression;
  setLocation: (_: LatLng) => void;
}

export const LocationMarker: React.FunctionComponent<Props> = ({ location, setLocation }) => {
  const map = useMapEvent(
    'click',
    (e: LeafletMouseEvent) => {
      setLocation(e.latlng);
      map.flyTo(e.latlng, map.getZoom());
    },
  );

  return location === undefined ? null : (
    <Marker position={location} />
  );
};
