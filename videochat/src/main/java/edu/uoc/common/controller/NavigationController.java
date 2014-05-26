/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.common.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author antonibertranbellido
 */

@Controller
public class NavigationController {

	@RequestMapping(value={"/", "index"}, method=RequestMethod.GET)
	public ModelAndView homePage() {
		return new ModelAndView("index");
	}
	@RequestMapping(value={"error"})
	public ModelAndView errorPage() {
		return new ModelAndView("error");
	}
	@RequestMapping(value={"errorLTI"})
	public ModelAndView errorLTIPage() {
		return new ModelAndView("errorLTI");
	}
	
}
