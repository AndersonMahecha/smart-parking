const url = `${process.env.VUE_APP_API_BASE_PATH}:${process.env.VUE_APP_API_PORT}/parking`;

function registerEntry(licenseCode) {
  return fetch(`${url}/entry?licenseCode=${licenseCode}&vehicleType=1`, {
    method: "POST",
  }).then((res) => res.json());
}

function registerExit(licenseCode) {
  return fetch(`${url}/exit`, {
    method: "POST",
    params: {
      licenseCode: licenseCode,
    },
  }).then((res) => res.json());
}

export { registerEntry, registerExit };
