/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.restful;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

/**
 *
 * @author USER
 */
@javax.ws.rs.ApplicationPath("tmsra")
public class ApplicationConfig extends Application {
    
    private Set<Object> singletons = new HashSet<Object>();
    private HashSet<Class<?>> classes = new HashSet<Class<?>>();
    
    public ApplicationConfig()
    {
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
        singletons.add(corsFilter);

        classes.add(vn.ra.restful.GenericResource.class);
    }
    
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public HashSet<Class<?>> getClasses(){
      return classes;
    }

//    @Override
//    public Set<Class<?>> getClasses() {
//        Set<Class<?>> resources = new java.util.HashSet<>();
//        addRestResourceClasses(resources);
//        return resources;
//    }
//
//    /**
//     * Do not modify addRestResourceClasses() method.
//     * It is automatically populated with
//     * all resources defined in the project.
//     * If required, comment out calling this method in getClasses().
//     */
//    private void addRestResourceClasses(Set<Class<?>> resources) {
//        resources.add(vn.ra.restful.GenericResource.class);
//    }
    
}
