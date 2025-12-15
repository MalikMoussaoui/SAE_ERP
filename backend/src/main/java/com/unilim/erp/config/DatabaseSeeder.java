package com.unilim.erp.config;

import com.unilim.erp.domain.UserRole;
import com.unilim.erp.domain.UserStatus;
import com.unilim.erp.entities.*;
import com.unilim.erp.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final UeRepository ueRepository;
    private final ResourceRepository resourceRepository;
    private final SaeRepository saeRepository;
    private final CourseRepository courseRepository;
    private final AppUserRepository appUserRepository;

    public DatabaseSeeder(DepartmentRepository departmentRepository,
                          UeRepository ueRepository,
                          ResourceRepository resourceRepository,
                          SaeRepository saeRepository,
                          CourseRepository courseRepository,
                          AppUserRepository appUserRepository) {
        this.departmentRepository = departmentRepository;
        this.ueRepository = ueRepository;
        this.resourceRepository = resourceRepository;
        this.saeRepository = saeRepository;
        this.courseRepository = courseRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (appUserRepository.count() > 0) {
            return;
        }

        System.out.println("Seeding : Création du personnel IUT (Admin, RH, Profs, Vacataires)...");
        Department deptInfo = new Department();
        deptInfo.setLabel("Informatique");
        departmentRepository.save(deptInfo);

        Department deptGea = new Department();
        deptGea.setLabel("GEA (Gestion)");
        departmentRepository.save(deptGea);

        AppUser admin = createUser("System", "Admin", "admin@unilim.fr", UserRole.ADMINISTRATEUR, UserStatus.ACTIVE);
        AppUser rh = createUser("Durand", "Sophie", "sophie.durand@unilim.fr", UserRole.RH, UserStatus.ACTIVE);
        AppUser respInfo = createUser("Turing", "Alan", "alan.turing@unilim.fr", UserRole.RESPONSABLE_PEDAGOGIQUE, UserStatus.ACTIVE);
        AppUser profJava = createUser("Lovelace", "Ada", "ada.lovelace@unilim.fr", UserRole.TEACHER, UserStatus.ACTIVE);
        AppUser profWeb = createUser("Berners-Lee", "Tim", "tim.berners@unilim.fr", UserRole.TEACHER, UserStatus.ACTIVE);
        AppUser referentBdd = createUser("Codd", "Edgar", "edgar.codd@unilim.fr", UserRole.REFERENT, UserStatus.ACTIVE);
        AppUser vacataire = createUser("Musk", "Elon", "elon.musk@tesla.com", UserRole.VACATAIRE, UserStatus.ACTIVE);
        AppUser profSuspendu = createUser("Dalton", "Joe", "joe.dalton@unilim.fr", UserRole.TEACHER, UserStatus.SUSPENDED);

        appUserRepository.saveAll(Arrays.asList(admin, rh, respInfo, profJava, profWeb, referentBdd, vacataire, profSuspendu));
        Ue ueDev = createUE("UE 1.1 - Développement", 1, deptInfo);
        Ue ueDonnees = createUE("UE 1.2 - Données", 1, deptInfo);
        ueRepository.saveAll(Arrays.asList(ueDev, ueDonnees));

        Resource rJava = createResource("R1.01 - Java", 10, 20, 30, ueDev);
        Resource rWeb = createResource("R1.02 - Web", 10, 15, 15, ueDev);
        Resource rBdd = createResource("R1.05 - SQL & Base de données", 12, 14, 20, ueDonnees);

        resourceRepository.saveAll(Arrays.asList(rJava, rWeb, rBdd));

        Sae saeSite = new Sae();
        saeSite.setTitle("SAE 1.01 - Site Web E-Commerce");
        saeSite.setHours(40);
        saeRepository.save(saeSite);
        createCourse(2, rJava, null, respInfo);
        createCourse(4, rJava, null, profJava);
        createCourse(4, rWeb, null, profWeb);
        createCourse(2, rBdd, null, referentBdd);
        Course coursProjet = new Course();
        coursProjet.setHours(12);
        coursProjet.setSae(saeSite);
        coursProjet.setTeachers(new HashSet<>());
        coursProjet.getTeachers().add(vacataire);
        courseRepository.save(coursProjet);

        System.out.println("Seeding terminé : Base prête avec RH, Vacataires et Admin !");
    }

    private AppUser createUser(String nom, String prenom, String email, UserRole role, UserStatus status) {
        AppUser user = new AppUser();
        user.setDisplayName(prenom + " " + nom);
        user.setEmail(email);
        user.setPasswordHash("1234");
        user.setPhone("0601020304");
        user.setRole(role);
        user.setStatus(status);
        return user;
    }

    private Ue createUE(String titre, int semestre, Department dept) {
        Ue ue = new Ue();
        ue.setTitle(titre);
        ue.setSemester(semestre);
        ue.setDepartment(dept);
        return ue;
    }

    private Resource createResource(String titre, int cm, int td, int tp, Ue ue) {
        Resource r = new Resource();
        r.setTitle(titre);
        r.setCmHours(cm);
        r.setTdHours(td);
        r.setTpHours(tp);
        r.setUe(ue);
        return r;
    }

    private void createCourse(int hours, Resource resource, Sae sae, AppUser prof) {
        Course c = new Course();
        c.setHours(hours);
        if (resource != null) c.setResource(resource);
        if (sae != null) c.setSae(sae);

        if (prof != null) {
            c.setTeachers(new HashSet<>());
            c.getTeachers().add(prof);
        }
        courseRepository.save(c);
    }
}