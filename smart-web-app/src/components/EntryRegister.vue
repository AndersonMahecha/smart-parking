<template>
  <h1 class="title">Registro de usuario</h1>
  <div class="container has-text-left">
    <div class="columns">
      <div class="column is-four-fifths">
        <div class="field">
          <label class="label">Nombre</label>
          <div class="control has-icons-left">
            <input class="input is-primary" type="text" placeholder="Nombre" />
            <span class="icon is-small is-left">
              <i class="fas fa-user"></i>
            </span>
          </div>
        </div>
        <div class="field">
          <label class="label">Primer apellido</label>
          <div class="control has-icons-left">
            <input
              type="text"
              class="input is-primary"
              placeholder="Primer apellido"
            />
            <span class="icon is-small is-left">
              <i class="fas fa-user"></i>
            </span>
          </div>
        </div>
        <div class="field">
          <label class="label">Segundo apellido</label>
          <div class="control has-icons-left">
            <input
              type="text"
              class="input is-primary"
              placeholder="Segundo apellido"
            />
            <span class="icon is-small is-left">
              <i class="fas fa-user"></i>
            </span>
          </div>
        </div>
        <div class="field">
          <label class="label">Tipo de documento</label>
          <div class="control">
            <div class="select is-primary">
              <select>
                <option value="CC">Cedula</option>
                <option value="TI">Tarjeta de identidad</option>
                <option value="CE">Cedula de extranjeria</option>
              </select>
            </div>
          </div>
        </div>
        <div class="field">
          <label class="label">Numero de documento</label>
          <div class="control">
            <input
              type="number"
              class="input is-primary"
              placeholder="Numero de documento"
            />
          </div>
        </div>
        <div class="field">
          <label class="label">Numero de contacto</label>
          <div class="control has-icons-left">
            <input
              type="number"
              class="input is-primary"
              placeholder="Numero de contacto"
            />
            <div class="icon is-small is-left">
              <i class="fa-solid fa-mobile"></i>
            </div>
          </div>
        </div>
        <div class="field">
          <label class="label">Email</label>
          <div class="control has-icons-left">
            <input type="email" class="input is-primary" placeholder="Email" />
            <span class="icon is-small is-left">
              <i class="fa-solid fa-envelope" />
            </span>
          </div>
        </div>

        <div class="field">
          <label class="label">Tipo de usuario</label>
          <div class="control">
            <div class="select is-primary">
              <select>
                <option value="ES">Estudiante</option>
                <option value="PR">Profesor</option>
                <option value="AD">Administrativo</option>
              </select>
            </div>
          </div>
        </div>

        <button class="button is-success">Crear usuario</button>
      </div>
    </div>
  </div>
</template>
<script>
export default {};
</script>