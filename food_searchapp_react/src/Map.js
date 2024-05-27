import React from 'react'
import {LoadScript, GoogleMap} from "@react-google-maps/api"

function Map() {
    const container = {
        width: "75%",
        height: "500px"
      };
      
      const position = {
        lat: 35.680959106959,
        lng: 139.76730676352
      };
  return (
    <div>
        <LoadScript googleMapsApiKey='APIキー'>
          <GoogleMap mapContainerStyle={container} center={position} zoom={15}>
          </GoogleMap>
        </LoadScript>
    </div>
  )
}

export default Map