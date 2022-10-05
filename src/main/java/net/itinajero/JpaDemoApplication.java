package net.itinajero;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import net.itinajero.model.Categoria;
import net.itinajero.model.Perfil;
import net.itinajero.model.Usuario;
import net.itinajero.model.Vacante;
import net.itinajero.repository.CategoriasRepository;
import net.itinajero.repository.PerfilesRepository;
import net.itinajero.repository.UsuariosRepository;
import net.itinajero.repository.VacantesRepository;

@SpringBootApplication
public class JpaDemoApplication implements CommandLineRunner{
	
	@Autowired
	private CategoriasRepository repoCategorias;
	
	@Autowired
	private VacantesRepository repoVacantes;
	
	@Autowired
	private UsuariosRepository repoUsuarios;
	
	@Autowired
	private PerfilesRepository repoPerfiles;

	public static void main(String[] args) {
		SpringApplication.run(JpaDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		buscarVacantesVariosEstatus();
	}
	
	private void buscarVacantesVariosEstatus() {
		String[] estatus = new String[] {"Eliminada", "Aprobada"};
		List<Vacante> lista = repoVacantes.findByEstatusIn(estatus);
		System.out.println("Registros Encontrados: " + lista.size());
		for (Vacante v : lista) {
			System.out.println(v.getId() + ": " + v.getNombre() + ": " + v.getEstatus());
		}
	}
	
	private void buscarVacantesSalario() {
		List<Vacante> lista = repoVacantes.findBySalarioBetweenOrderBySalarioDesc(7000, 14000);
		System.out.println("Registros Encontrados: " + lista.size());
		for (Vacante v : lista) {
			System.out.println(v.getId() + ": " + v.getNombre() + ": $" + v.getSalario());
		}
	}
	
	private void buscarVacantesPorDestacadoEstatus() {
		List<Vacante> lista = repoVacantes.findByDestacadoAndEstatusOrderByIdDesc(1, "Aprobada");
		System.out.println("Registros Encontrados: " + lista.size());
		for (Vacante v : lista) {
			System.out.println(v.getId() + ": " + v.getNombre() + ": " + v.getEstatus() + ": " + v.getDestacado());
		}
	}
	
	private void buscarVacantesPorEstatus() {
		List<Vacante> lista = repoVacantes.findByEstatus("Aprobada");
		System.out.println("Registros Encontrados: " + lista.size());
		for (Vacante v : lista) {
			System.out.println(v.getId() + ": " + v.getNombre() + ": " + v.getEstatus());
		}
	}
	
	/**
	 * Metodo para buscar un usuario y desplegar sus perfiles asociados.
	 */
	public void buscarUsuario() {
		Optional<Usuario> optional = repoUsuarios.findById(1);
		if (optional.isPresent()) {
			Usuario u = optional.get();
			System.out.println("Usuario: " + u.getNombre());
			System.out.println("Perfiles asignados");
			for (Perfil p : u.getPerfiles()) {
				System.out.println(p.getPerfil());
			}
		}else {
			System.out.println("Usuario no encontrado");
		}
	}
	
	/**
	 * Crear un usuario con 2 perfiles ("ADMINISTRADOR", "USUARIO")
	 */
	private void crearUsuarioConDosPerfiles() {
		Usuario user = new Usuario();
		user.setNombre("Ivan Tinajero");
		user.setEmail("ivanetinajero@gmail.com");
		user.setFechaRegistro(new Date());
		user.setUsername("itinajero");
		user.setPassword("12345");
		user.setEstatus(1);
		
		Perfil per1 = new Perfil();
		per1.setId(2);
		
		Perfil per2 = new Perfil();
		per2.setId(3);
		
		user.agregar(per1);
		user.agregar(per2);
		
		repoUsuarios.save(user); 
	}
	
	/**
	 * Metodo para crear PERFILES / ROLES
	 */
	private void crearPerfilesAplicacion() {
		repoPerfiles.saveAll(getPerfilesAplicacion());
	}
	
	private void guardarVacante() {
		Vacante vacante = new Vacante();
		vacante.setNombre("Profesor de Matematicas");
		vacante.setDescripcion("Escuela Primaria solicita Profesor para curso de Matematicas");
		vacante.setFecha(new Date());
		vacante.setSalario(8500.0);
		vacante.setEstatus("Aprobada");
		vacante.setDestacado(0);
		vacante.setImagen("escuela.png");
		vacante.setDetalles("<h1>Los requisitos para profesor de Matematicas</h1>");
		Categoria cat = new Categoria();
		cat.setId(12);
		vacante.setCategoria(cat);
		repoVacantes.save(vacante);
	}
	
	private void buscarVacantes() {
		List<Vacante> lista = repoVacantes.findAll();
		for(Vacante v : lista) {
			System.out.println(v.getId() + " " + v.getNombre() + "-> " + v.getCategoria().getNombre());
		}
	}
	
	//Metodos de Jpa
	
	public void buscarTodosPaginacionOrdenados() {
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(0, 5,Sort.by("nombre").descending()));
		System.out.println("Total Registros: " + page.getTotalElements());
		System.out.println("Total Paginas: " + page.getTotalPages());
		for (Categoria c : page.getContent()) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	private void buscarTodosPaginacion() {
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(0, 5));
		System.out.println("Total Registros: " + page.getTotalElements());
		System.out.println("Total Paginas: " + page.getTotalPages());
		for (Categoria c : page.getContent()) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	private void buscarTodosOrdenados() {
		Iterable<Categoria> categorias = repoCategorias.findAll(Sort.by("nombre").descending());
		for (Categoria cat : categorias) {
			System.out.println(cat);
		}
	}
	
	private void borrarTodoEnBloque() {
		repoCategorias.deleteAllInBatch();
	}
	
	private void buscarTodosJpa() {
		List<Categoria> categorias = repoCategorias.findAll();
		for (Categoria c : categorias) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	//Operaciones CRUD y Metodos
	
	private void guardarTodas() {
		List<Categoria> categorias = getListaCategorias();
		repoCategorias.saveAll(categorias);
	}
	
	private void existeId() {
		boolean existe = repoCategorias.existsById(4);
		System.out.println("La Categoria existe: " + existe);
	}
	
	private void buscarTodos() {
		Iterable<Categoria> categorias = repoCategorias.findAll();
		for (Categoria cat : categorias) {
			System.out.println(cat);
		}
	}
	
	private void encontrarPorIds() {
		List<Integer> ids = new LinkedList<Integer>();
		ids.add(4);
		ids.add(6);
		Iterable<Categoria> categorias = repoCategorias.findAllById(ids);
		for (Categoria cat : categorias) {
			System.out.println(cat);
		}
	}
	
	private void eliminarTodos() {
		repoCategorias.deleteAll();
	}
	
	private void conteo() {
		long count = repoCategorias.count();
		System.out.println("Total Categorias: " + count);
	}
	
	private void eliminar() {
		int idCategoria = 1;
		repoCategorias.deleteById(idCategoria);
	}
	
	private void modificar() {
		Optional<Categoria> optional = repoCategorias.findById(2);
		if (optional.isPresent()) {
			Categoria catTmp = optional.get();
			catTmp.setNombre("Ing. de Software");
			catTmp.setDescripcion("Desarrollo de Sistemas");
			repoCategorias.save(catTmp);
			System.out.println(optional.get());
		}
		else
			System.out.println("Categoria no encontrada nuv");
	}
	
	private void buscarPorId() {
		Optional<Categoria> optional = repoCategorias.findById(1);
		if (optional.isPresent()) 
			System.out.println(optional.get());
		else
			System.out.println("Categoria no encontrada nuv");
	}
	
	private void guardar() {
		Categoria cat = new Categoria();
		cat.setNombre("Finanzas");
		cat.setDescripcion("Trabajos relacionados con Finanzas y Contabilidad");
		repoCategorias.save(cat);
		System.out.println(cat);
	}
	
	private List<Categoria> getListaCategorias(){
		List<Categoria> lista = new LinkedList<Categoria>();
		
		// Categoria 1
		Categoria cat1 = new Categoria();
		cat1.setNombre("Contabilidad");
		cat1.setDescripcion("Descripcion de la categoria Contabilidad");
		
		// Categoria 2
		Categoria cat2 = new Categoria();
		cat2.setNombre("Ventas");
		cat2.setDescripcion("Trabajos relacionados con Ventas");
		
					
		// Categoria 3
		Categoria cat3 = new Categoria();
		cat3.setNombre("Comunicaciones");
		cat3.setDescripcion("Trabajos relacionados con Comunicaciones");
		
		lista.add(cat1);			
		lista.add(cat2);
		lista.add(cat3);
		return lista;
	}
	
	/**
	 * Metodo que regresa una lista de objetos de tipo Perfil que representa los diferentes PERFILES 
	 * O ROLES que tendremos en nuestra aplicación de Empleos
	 * @return
	 */
	private List<Perfil> getPerfilesAplicacion(){		
		List<Perfil> lista = new LinkedList<Perfil>();
		Perfil per1 = new Perfil();
		per1.setPerfil("SUPERVISOR");
		
		Perfil per2 = new Perfil();
		per2.setPerfil("ADMINISTRADOR");
		
		Perfil per3 = new Perfil();
		per3.setPerfil("USUARIO");
		
		lista.add(per1);
		lista.add(per2);
		lista.add(per3);
		
		return lista;
	}

}
