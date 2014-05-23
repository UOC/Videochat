package org.red5.server.script.jython;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.webapp.WebAppContext;
import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyJavaInstance;
import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import org.springframework.scripting.ScriptCompilationException;
import org.springframework.scripting.ScriptFactory;
import org.springframework.scripting.ScriptSource;
import org.springframework.util.Assert;

/**
 * {@link org.springframework.scripting.ScriptFactory} implementation for a
 * Python script.
 * 
 * @author Yan Weng
 * @see <a href="http://yanweng.blogspot.com/2006/02/prototype-of-jython-scriptfactory-for.html">A prototype of Jython ScriptFactory for Spring Framework</a>
 */
public class JythonScriptFactory implements ScriptFactory {

	private static Logger logger = Logger.getLogger(JythonScriptFactory.class);
	
	private final String scriptSourceLocator;
	private final Class[] scriptInterfaces;
	private final Object[] arguments;

	public JythonScriptFactory(String scriptSourceLocator) {
		Assert.hasText(scriptSourceLocator);
		this.scriptSourceLocator = scriptSourceLocator;
		this.scriptInterfaces = new Class[]{};
		this.arguments = null;
	}
	
	public JythonScriptFactory(String scriptSourceLocator, Class[] scriptInterfaces) {
		Assert.hasText(scriptSourceLocator);
		Assert.notEmpty(scriptInterfaces);
		this.scriptSourceLocator = scriptSourceLocator;
		this.scriptInterfaces = scriptInterfaces;
		this.arguments = null;
	}
	
	public JythonScriptFactory(String scriptSourceLocator, Class[] scriptInterfaces, Object[] arguments) {
		Assert.hasText(scriptSourceLocator);
		Assert.notEmpty(scriptInterfaces);
		this.scriptSourceLocator = scriptSourceLocator;
		this.scriptInterfaces = scriptInterfaces;
		if (arguments == null || arguments.length == 0) {
			this.arguments = null;
		} else {
			this.arguments = arguments;
		}
	}
	
	/** {@inheritDoc} */
    public String getScriptSourceLocator() {
		return scriptSourceLocator;
	}

	/** {@inheritDoc} */
    public Class[] getScriptInterfaces() {
		return scriptInterfaces;
	}

	/** {@inheritDoc} */
    public boolean requiresConfigInterface() {
		return true;
	}

	/** {@inheritDoc} */
    public Object getScriptedObject(ScriptSource scriptSourceLocator, Class[] scriptInterfaces)
			throws IOException, ScriptCompilationException {
    	// TODO: how to do this when running under Tomcat?
    	ContextHandler handler = WebAppContext.getCurrentWebAppContext();
    	String basePath = "";
    	if (handler != null) {
    		File root = handler.getBaseResource().getFile();
    		if (root != null && root.exists()) {
    			basePath = root.getAbsolutePath() + File.separator + "WEB-INF" + File.separator;
    		}
    	}
    	
		String strScript = scriptSourceLocator.getScriptAsString();
		if (scriptInterfaces.length > 0) {   
			try {
				PySystemState state = new PySystemState();
				if (!"".equals(basePath)) {
					// Add webapp paths that can contain classes and .jar files to python search path
					state.path.insert(0, Py.newString(basePath + "classes"));
					File jarRoot = new File(basePath + "lib");
					if (jarRoot.exists()) {
						for (String filename: jarRoot.list(new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return (name.endsWith(".jar"));
							}
						})) {
							state.path.insert(1, Py.newString(basePath + "lib" + File.separator + filename));
						}
					}
				}
				PythonInterpreter interp = new PythonInterpreter(null, state);
				interp.exec(strScript);
				PyObject getInstance = interp.get("getInstance");
				if (!(getInstance instanceof PyFunction)) {
					throw new ScriptCompilationException("\"getInstance\" is not a function.");
				}
				PyObject _this;
				if (arguments == null) {
					_this = ((PyFunction) getInstance).__call__();
				} else {
					PyObject[] args = new PyObject[arguments.length];
					for (int i=0; i<arguments.length; i++) {
						args[i] = new PyJavaInstance(arguments[i]);
					}
					_this = ((PyFunction) getInstance).__call__(args);
				}
				return _this.__tojava__(scriptInterfaces[0]);
			} catch (Exception ex) {
				logger.error("Error while loading script.", ex);
				if (ex instanceof IOException) {
					// Raise to caller
					throw (IOException) ex;
				} else if (ex instanceof ScriptCompilationException) {
					// Raise to caller
					throw (ScriptCompilationException) ex;
				}
				
				throw new ScriptCompilationException(ex.getMessage());
			}
		}
		logger.error("No scriptInterfaces provided.");
		return null;
	}


	public Class getScriptedObjectType(ScriptSource arg0) throws IOException, ScriptCompilationException {
		// TODO Auto-generated method stub
		return null;
	}

}
