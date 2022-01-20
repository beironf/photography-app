import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import * as serviceWorker from 'serviceWorker';
import 'bootstrap/dist/css/bootstrap.min.css';

// Fixes for leaflet package
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';
const DefaultIcon = L.icon({
  iconUrl: icon,
  shadowUrl: iconShadow,
  iconAnchor: [12.5, 41],
});
L.Popup.prototype.options.offset = L.point(1, -18);
L.Marker.prototype.options.icon = DefaultIcon;

ReactDOM.render(<App />, document.getElementById('root'));
serviceWorker.unregister();
