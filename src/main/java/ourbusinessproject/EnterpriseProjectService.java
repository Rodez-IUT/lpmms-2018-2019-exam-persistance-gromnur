package ourbusinessproject;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class EnterpriseProjectService {

    @PersistenceContext
    private EntityManager entityManager;

    public Project saveProjectForEnterprise(Project project, Enterprise enterprise) {
        Enterprise ancienneEnt = project.getEnterprise();
        Enterprise ent = saveEnterprise(enterprise);
        project.setEnterprise(ent);
        Project p = entityManager.merge(project);
        ent.addProject(p);
        entityManager.persist(p);
        entityManager.flush();
        return p;
    }

    public Enterprise saveEnterprise(Enterprise enterprise) {
        Enterprise e = entityManager.merge(enterprise); 
        entityManager.persist(e);
        entityManager.flush();
        return e;
    }

    public Project findProjectById(Long id) {
        return entityManager.find(Project.class, id);
    }

    public Enterprise findEnterpriseById(Long id) {
        return entityManager.find(Enterprise.class, id);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Project> findAllProjects() {
        TypedQuery<Project> query = entityManager.createQuery("select p from Project p join fetch p.enterprise order by p.title", Project.class);
        return query.getResultList();
    }
}
