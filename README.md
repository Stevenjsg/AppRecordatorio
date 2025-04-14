# ✅ Lista de Tareas - App de Recordatorios

## 🔴 Prioridad Alta

- [ ] **Validación de datos en el formulario de creación/edición**
  - Verificar campos obligatorios como título, tipo, horas, etc.
  - Mostrar errores en los campos si están vacíos o mal formateados.
  - Prevenir el guardado de recordatorios incompletos.

- [ ] **Eliminar recordatorios**
  - Activar modo de selección múltiple desde un botón en el `TopAppBar`.
  - Mostrar `Checkbox` en cada card.
  - Cambiar el color del botón de eliminar cuando hay al menos una card seleccionada.
  - Eliminar todos los seleccionados al confirmar.

- [ ] **Diseño mejorado para input de hora (como la app de reloj)**
  - Reemplazar `OutlinedTextField` por un `TimePicker` moderno.
  - Facilitar la selección de horas para los campos necesarios.

## 🟡 Prioridad Media

- [ ] **Notificaciones cuando llegue la próxima hora del recordatorio**
  - Programar alarmas/notifications locales con `WorkManager` o `AlarmManager`.
  - Mostrar la notificación al llegar la hora del recordatorio.

- [ ] **Confirmación del recordatorio tras notificación**
  - Notificación debe tener acciones: “Sí” o “No”.
  - Si el usuario confirma:
    - Actualizar `ultimaHora` con el valor de `proximaHora`.
    - Calcular nueva `proximaHora` sumando el intervalo definido.

## 🟢 Prioridad Baja / Mejoras futuras

- [ ] Animaciones y transiciones suaves entre pantallas.
- [ ] Guardar las selecciones recientes del selector de tipo para usarlas como predeterminadas.
- [ ] Modo oscuro opcional.
- [ ] Sincronización con la nube (para respaldos).
