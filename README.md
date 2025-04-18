# 🗺️ Mapa de Ruta - App de Recordatorios

## ✅ Completado

- [x] Estructura del proyecto con Jetpack Compose
- [x] Data class `Recordatorio` con soporte para distintos tipos (agua, ayuno, suplemento, otro)
- [x] Formulario para crear y editar recordatorios
- [x] Validación de campos del formulario
- [x] Input de horas con `TimePickerDialog`
- [x] Base de datos con Room (DAO, entidad, repositorio)
- [x] Uso de Flow con Room para actualización en tiempo real
- [x] Mostrar recordatorios en tarjetas (Card)
- [x] Filtro visual con badges (LazyRow) por tipo
- [x] Soporte para editar recordatorios
- [x] Eliminación múltiple con selección visual
- [x] Programar notificaciones con AlarmManager
- [x] Configuración de NotificationChannel
- [x] Manejo de permisos de notificación (Android 13+)
- [x] Notificaciones con acciones: "Hecho" / "Ignorar"
- [x] Confirmar recordatorio desde la notificación (ConfirmarReceiver)
- [x] Actualizar `ultimaHora` y `proximaHora` automáticamente
- [x] Cancelar la notificación al confirmar
- [x] Reprogramar la próxima notificación automáticamente
- [x] Actualización automática de la UI con StateFlow y collectAsState()

---

## 🔧 En progreso / pendientes

- [ ] Implementar `IgnorarReceiver` (cierra notificación sin actualizar)
- [ ] Validar lógica avanzada de repetición para ayuno (ayuno y ventana de comida)
- [ ] Soporte para `SCHEDULE_EXACT_ALARM` (Android 12+)
- [ ] Traducción e internacionalización usando `strings.xml`
- [ ] Mostrar estado visual (activo/inactivo) con iconos más discretos
- [ ] Rediseño visual más limpio (tema, iconos, animaciones)
- [ ] Implementar backup y restauración de recordatorios
- [ ] Historial o estadísticas de cumplimiento
- [ ] Notificación persistente si no se confirma

---

## 🧠 Ideas futuras

- [ ] Integración con Google Calendar
- [ ] Soporte para Wear OS (smartwatch)
- [ ] Integración con voz (TTS / Google Assistant)
- [ ] Modo oscuro personalizado

