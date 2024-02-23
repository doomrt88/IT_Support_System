package loginBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named(value="indexBean")
@RequestScoped
public class IndexJSFBean {
	
	private String mensaje = "Prueba Login 2";

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
