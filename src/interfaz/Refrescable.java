package interfaz;

//La implementan los paneles cuyo contenido depende de datos que se editan en otras pestañas, para que la ventana principal pueda recargarlos cada
// vez que el usuario cambia de pestana.
public interface Refrescable {

	void refrescar();
}