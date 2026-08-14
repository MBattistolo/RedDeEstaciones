import java.time.LocalDate;
import java.util.*;

public class Red {
	
	private TipoContaminante[] tiposContaminante;
	private Estacion[] estaciones;
	private Medicion[] mediciones;
	private Personal[] personal;
	
	public Red() {
		this.tiposContaminante = new TipoContaminante[0];
		this.estaciones = new Estacion[0];
		this.mediciones = new Medicion[0];
		this.personal = new Personal[0];
	}
	
	//en esta sección van todos los métodos de buscar, todos estos siguen la misma lógica de usar un while para la búsqueda del índice
	//esto con el objetivo de usarlo para posteriores métodos como eliminar
	public Integer buscarTipoContaminante(String codigo) {
		int i = 0;
		
		while(i<tiposContaminante.length && !codigo.equals(tiposContaminante[i].getCodigo()))
			i++;
		
		if (i==tiposContaminante.length) {
			return null;
		}
		return i;
	}
	
	public Integer buscarEstacion(String codigoEstacion) {
		int i = 0;
		
		while(i<estaciones.length && !codigoEstacion.equals(estaciones[i].getCodigo()))
			i++;
		
		if (i==estaciones.length) {
			return null;
		}
		return i;
	}
	
	public Integer buscarMedicion(String codigo) {
		int i = 0;
		
		while(i<mediciones.length && !codigo.equals(mediciones[i].getCodigo()))
			i++;
		
		if (i==mediciones.length) {
			return null;
		}
		return i;
		}
	
	public Integer buscarPersonal(String documento) {
		int i = 0;
		
		while(i<personal.length && !documento.equals(personal[i].getNumeroDocumento()))
			i++;
		
		if (i==personal.length) {
			return null;
		}
		return i;
	}
	
	
	//en esta sección van todos los métodos relacionados a adicionar, cabe resaltar que se utilizó una excepción para validar
	//que no haya entradas duplicadas en los arreglos respectivos, todos estos métodos siguen la misma lógica de copiar el arreglo usando copyOf
	//con longitud +1 y agregar el nuevo objeto en ese nuevo espacio
	public void addTipoContaminante(String codigo, String nombreContaminante, double valorMinimo, double valorMaximo) throws ECodigoDuplicado{
		
		if (buscarTipoContaminante(codigo)!= null) {
			throw new ECodigoDuplicado("tipo de contaminante", codigo);
		}
		TipoContaminante t = new TipoContaminante(codigo, nombreContaminante, valorMinimo, valorMaximo);
		
		tiposContaminante = Arrays.copyOf(tiposContaminante, tiposContaminante.length+1);
		tiposContaminante[tiposContaminante.length-1] = t;
	}
	
	public void addEstacion(String codigo, String nombre, String comuna, LocalDate fechaInstalacion) throws ECodigoDuplicado{
		
		if (buscarEstacion(codigo)!=null) {
			throw new ECodigoDuplicado("estacion", codigo);
		}
		Estacion e = new Estacion(codigo, nombre, comuna, fechaInstalacion);
		
		estaciones = Arrays.copyOf(estaciones, estaciones.length+1);
		estaciones[estaciones.length-1] = e;
	}
	
	public void addPersonal(String tipoDocumento, String numeroDocumento, String nombre, String apellido, char genero, String direccion,
			String telefono) throws ECodigoDuplicado{
		
		if (buscarPersonal(numeroDocumento)!=null) {
			throw new ECodigoDuplicado("personal", numeroDocumento);
		}
		
		Personal p = new Personal(tipoDocumento, numeroDocumento, nombre, apellido, genero, direccion, telefono);
		
		personal = Arrays.copyOf(personal, personal.length+1);
		personal[personal.length-1] = p;
	}

	//el add de medicion aire y medicion ruido necesitan una validación extra ya que estos también pertenecen a una estación concreta
	//para ello también debemos chequear que la estación a la cuál se va a adicionar la medicion exista
	public void addMedicionAire(String codigoEstacion, String codigoMedicion, Personal tecnicoQueRegistra, LocalDate fechaTomada, 
			double valorMedido, TipoContaminante tipoContaminante) throws ECodigoNoExiste, ECodigoDuplicado{
		
		if (buscarMedicion(codigoMedicion) != null) {
			throw new ECodigoDuplicado("medicion", codigoMedicion);
		}
		
		Integer i = buscarEstacion(codigoEstacion);
		if (i==null) {
			throw new ECodigoNoExiste("estacion", codigoEstacion);
		}
		
		MedicionAire ma = new MedicionAire(codigoMedicion, tecnicoQueRegistra, fechaTomada, valorMedido, tipoContaminante);
		
		mediciones = Arrays.copyOf(mediciones, mediciones.length+1);
		mediciones[mediciones.length-1] = ma;
		
		Estacion e = estaciones[i];
		
		e.addMedicion(ma);
	}

	public void addMedicionRuido(String codigoEstacion, String codigoMedicion, Personal tecnicoQueRegistra, LocalDate fechaTomada,
			double valorMedido, Jornada jornada) throws ECodigoNoExiste, ECodigoDuplicado{
		
		if (buscarMedicion(codigoMedicion) != null) {
			throw new ECodigoDuplicado("medicion", codigoMedicion);
		}
		
		Integer i = buscarEstacion(codigoEstacion);
		
		if (i==null) {
			throw new ECodigoNoExiste("estacion", codigoEstacion);
		}
		
		MedicionRuido mr = new MedicionRuido(codigoMedicion, tecnicoQueRegistra, fechaTomada, valorMedido, jornada);
		
		mediciones = Arrays.copyOf(mediciones, mediciones.length+1);
		mediciones[mediciones.length-1] = mr;
		
		Estacion e = estaciones[i];
		e.addMedicion(mr);
		}
	

	//en esta sección siguen los métodos relacionados a eliminar cosas, cada uno de estos tiene una lógica más propia por lo que voy a ser más
	//descriptivo en cada uno de estos métodos, sin embargo se puede evidenciar que se sigue un patrón muy similar entre ellos
	
	public void eliminarTipoContaminante(String codigo) throws ECodigoNoExiste, ETipoContaminanteEnUso{
	//En este método estamos realizando dos validaciones para poder eliminar un tipo contaminante, la primera es que si no existe, lógicamente 
	//no lo podemos borrar, la segunda requiere de un supuesto y es que para nuestro caso vamos a suponer que los tipos de contaminante que 
	//sean empleados en alguna medicion de aire no se pueden eliminar ya que se encuentran en uso por parte de la medición y son fundamentales
	//para su correcto funcionamiento
	
		//en este primer bloque determinamos si el tipo de contaminante con el código buscado si existe
		Integer i = buscarTipoContaminante(codigo);
		if (i==null) {
			throw new ECodigoNoExiste("tipo de contaminante", codigo); 
		}
		//para este segundo bloque hacemos la validación correspondiente a la determinación de si este tipo de contaminante existe en una medición 
		//concreta
		for(int j = 0; j<mediciones.length;j++) {
			if (mediciones[j] instanceof MedicionAire) {
				MedicionAire ma = (MedicionAire) mediciones[j];
				if (ma.getTipoContaminante().getCodigo().equals(codigo)) {
					throw new ETipoContaminanteEnUso(codigo);
				}
			}
		}
		//llegamos a este tercer bloque si se cumplieron los dos filtros anteriores, en este paso es donde eliminamos el tipo de contaminante ingresado
		TipoContaminante[] aux = new TipoContaminante[tiposContaminante.length-1];
		int k = 0;
		for (int j = 0; j<tiposContaminante.length;j++) {
			if (j!=i) {
				aux[k] = tiposContaminante[j];
				k++;
			}
		}
		tiposContaminante = aux;
	}
	
	public void eliminarEstacion(String codigo) throws ECodigoNoExiste{
	//este método tiene 2 tareas, eliminar la estación y eliminar las mediciones correspondientes a dicha estación ya que no tiene sentido
	//que queden mediciones de una estación que ya no existe en el arreglo mediciones de Red
	
		//se hace una primera validación de que la estación buscada si exista
		Integer i = buscarEstacion(codigo);
		
		if (i==null) {
			throw new ECodigoNoExiste("estacion", codigo);
		}
		
		//este primer bloque esta eliminando las mediciones del arreglo que se guarda en Red, usamos un for para recorrer el arreglo de mediciones
		//buscando aquellas que no pertenezcan a la estación a borrar en particular, el critero es que si una medicion no esta en una estación
		//buscarla con el método buscarMedicion nos va a retornar null y este indicador nos demuestra que dicha medicion se debe conservar
		Estacion e = estaciones[i];
		Medicion[] auxM = new Medicion[mediciones.length-e.getMediciones().length];
		int k = 0;
		for (int j = 0; j<mediciones.length;j++) {
			if (e.buscarMedicion(mediciones[j].getCodigo()) == null){
				auxM[k] = mediciones[j];
				k++;
			}
		}
		//al final lo que queda es lo que se reasigna a mediciones, esto ya que las mediciones que dieron null es porque no estaban en dicha
		//estación y no deberían ser eliminadas ya que pertenecen a otras
		mediciones = auxM;
		
		//Este segundo bloque se compone de otro for y esta es básicamente la otra forma de hacer eliminar pero sin copyOf como aparece en el 
		//método de Estacion, en este caso hacemos algo similar al ciclo de arriba, sino que esta vez usando el índice de la estación a borrar
		//voy a excluir dicho indice de la reasignación en el arreglo estaciones, cabe resaltar que esto se hace usando un arreglo auxiliar de 
		//tamaño -1 al de estaciones para que al copiar el arreglo nos aseguremos de que no incluimos un objeto que en este caso es el que borramos
		Estacion[] aux = new Estacion[estaciones.length-1];
		k = 0;
		for (int j = 0; j<estaciones.length;j++) {
			if (j!=i) {
				aux[k] = estaciones[j];
				k++;
			}
		}
		estaciones = aux;	
	}
	
	public void eliminarPersonal(String codigo) throws ECodigoNoExiste, EPersonalConMediciones{
	//En este método se elimina el personal, sin embargo para poder ser eliminado debe pasar por dos validaciones, la primera que exista
	//para la segunda validación vamos a trabajar bajo el supuesto de que un empleado que haya realizado al menos una medicion no puede ser borrado
	//ya que es como si no estuviese haciendo nada y lo queremos expulsar 
	
		//primera validación la cuál se encarga de que el empleado exista llamando al método buscar personal
		Integer i = buscarPersonal(codigo);
		if (i==null) {
			throw new ECodigoNoExiste("personal", codigo);
		}
		
		//acá hacemos la segunda validación donde debemos determinar si el empleado no tiene registros de mediciones 
		Personal p = personal[i];
		for (int j = 0; j<mediciones.length;j++) {
			if(mediciones[j].getTecnicoRegistro() == p) {
				throw new EPersonalConMediciones(codigo);
			}
		}
		
		//esta es la forma convencional de eliminar como se ha venido haciendo en otros métodos
		Personal[] aux = new Personal[personal.length-1];
		int k = 0;
		for (int j = 0; j<personal.length;j++) {
			if (j!=i) {
				aux[k] = personal[j];
				k++;
			}
		}
		personal = aux;	
	}
	
	public void eliminarMedicion(String codigo) throws ECodigoNoExiste{
	//En este método eliminar una medición implica eliminarla del arreglo en Red y del arreglo en Estacion
	
		Integer i = buscarMedicion(codigo);
		if (i==null) {
			throw new ECodigoNoExiste("medicion", codigo);
		}
		
		//este for each se encarga de eliminar la medición de la o las estaciones donde este presente, con esto damos a luz a otro supuesto
		//y es que una misma medición puede estar en más de una estación a la vez
		for (Estacion e : estaciones) {
			Integer idx = e.buscarMedicion(codigo);
			if(idx!=null) {
				e.eliminarMedicion(codigo);
			}
		}	
		
		Medicion[] aux = new Medicion[mediciones.length-1];
		int k = 0;
		for (int j = 0; j<mediciones.length;j++) {
			if(j!=i) {
				aux[k] = mediciones[j];
				k++;
			}
		}
		mediciones = aux;
	}
	
	//los siguientes métodos son propios de la clase, su objetivo es mostrar polimorfismo y el funcionamiento de algunos agentes del sistema
	
	//este método nos va a mostrar cuáles son las estaciones que en ese momento tienen mediciones críticas
	public Estacion[] estacionesConMedicionCritica() {
		
		//el método comienza creando un arreglo que va a crecer de acuerdo a las estaciones encontradas que cumplan la condición de criticas
		Estacion[] estacionesCriticas = new Estacion[0];
		
		//se utiliza un for each para recorrer todas las estaciones registradas en Red
		for (Estacion e : estaciones) {
			
			Medicion[] mes = e.getMediciones(); //obtenemos las mediciones de cada una de estas estaciones para después chequear cuáles son criticas
			int i = 0;
			while(i<mes.length && !mes[i].critica()) //este while es una especie de buscar criticas dado un arreglo de mediciones como lo es mes
				i++;
			//salimos del while sea encontrando el índice de la primera medición crítica o no encontrando nada, cabe resaltar que el no encontrar
			//nada si es válido para este caso ya que una estación puede no tener mediciones criticas
			
			if (i<mes.length) { //En caso de que encontremos algo lo vamos a agregar al arreglo declarado al inicio del método con copyOf
				estacionesCriticas = Arrays.copyOf(estacionesCriticas, estacionesCriticas.length+1);
				estacionesCriticas[estacionesCriticas.length-1] = e; 
			}
		}
		return estacionesCriticas;
	}
	
	//este método es una versión más específica del primero, su trabajo sera que dado el codigo de un contaminante, nos devuelva un arreglo
	//con las estaciones que llevan dicho contaminante en un nivel crítico
	public Estacion[] estacionesConMedicionAireCritica(String codigoContaminante) throws ECodigoNoExiste{
		
		//comenzamos validando si el codigo del contaminante existe, si no se lanza la excepción
		if (buscarTipoContaminante(codigoContaminante)==null) {
			throw new ECodigoNoExiste("tipo de contaminante", codigoContaminante);
		}
		
		//se crea un arreglo con el mismo objetivo del creado en el método anterior
		Estacion[] estacionesCriticas = new Estacion[0];
		
		for (Estacion e : estaciones) { //usamos el mismo for each para recorrer las estaciones
			
			Medicion[] mes = e.getMediciones();
			int i = 0;
			boolean encontrada = false; //este método requiere de una bandera para darle otra salida al while
			while(i<mes.length && !encontrada) {
				
				if (mes[i] instanceof MedicionAire) {//usamos instanceof para que solamente pasen las mediciones de aire concretas
					MedicionAire ma = (MedicionAire) mes[i]; //se hace el casteo para poder usar los métodos de MedicionAire en si
				
					if(ma.getTipoContaminante().getCodigo().equals(codigoContaminante) && ma.critica()) {
						//si el codigo del tipo de contaminante de esa MedicionAire coincide con el buscado, significa que encontramos
						//una medicion de ese contaminante en la estación, además si este es crítico se acaba el while y se cambia la bandera
						encontrada = true;
					}
				}
				if (!encontrada) {
					i++; //si en esa iteración no se encuentra pasa a la siguiente
				}
			}
			if (encontrada) {//una vez encontrado se agrega al arreglo de estacionesCriticas tal cuál como en el método anterior
				estacionesCriticas = Arrays.copyOf(estacionesCriticas, estacionesCriticas.length+1);
				estacionesCriticas[estacionesCriticas.length-1] = e;
			}		
		}
		return estacionesCriticas;
	}

	public TipoContaminante[] getTiposContaminante() {
		return tiposContaminante;
	}

	public Medicion[] getMediciones() {
		return mediciones;
	}

	public Estacion[] getEstaciones() {
		return estaciones;
	}

	public Personal[] getPersonal() {
		return personal;
	}
}