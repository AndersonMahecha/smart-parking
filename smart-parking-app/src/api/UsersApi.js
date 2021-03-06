const url = `${process.env.VUE_APP_API_BASE_PATH}:${process.env.VUE_APP_API_PORT}/users`;

function createUser(user) {
  return fetch(url, {
    method: "POST",
    body: JSON.stringify(user),
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => res.json());
}

function getUsers() {
  return fetch(url, {
    method: "GET",
  }).then((res) => res.json());
}

function getUser(licenseCode = null, documentNumber = null) {
  return fetch(`${url}/find`, {
    method: "GET",
    params: {
      licenseCode: licenseCode,
      documentNumber: documentNumber,
    },
  }).then((res) => res.json());
}

export default { getUser, getUsers, createUser };
