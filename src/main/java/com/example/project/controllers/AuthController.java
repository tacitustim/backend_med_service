package com.example.project.controllers;


import com.example.project.model.Doctor;
import com.example.project.model.Patient;
import com.example.project.model.Users;

import com.example.project.service.DoctorService;
import com.example.project.service.PatientService;
import com.example.project.service.UsersDetailsService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {



    private final UsersDetailsService usersDetailsService;
    private final DoctorService doctorService;
    private final PatientService patientService;





    @Autowired
    public AuthController(UsersDetailsService usersDetailsService, DoctorService doctorService, PatientService patientService) {

        this.usersDetailsService = usersDetailsService;
        this.doctorService = doctorService;

        this.patientService = patientService;
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("doctors", doctorService.findAll());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerPatient( Model model,
                                   @ModelAttribute("doct") Doctor doctor,
                                   @ModelAttribute("patient") @Valid Patient patient,
                                   BindingResult bindingResult,
                                   @RequestParam(name = "patient_pass") String patient_pass){

        if(patient_pass==null || patient_pass.equalsIgnoreCase("")){
            patient_pass="admin";
        }


        patient.setDoctor(doctorService.getDoctor(patient.getDoctor().getId()));

        patient.setUser(usersDetailsService.register(new Users(null, patient_pass, "ROLE_PATIENT")));

        patientService.addPatient(patient);

        return "redirect:/auth/register" + patient.getUser().getId();
    }




}
