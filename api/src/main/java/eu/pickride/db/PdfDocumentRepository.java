package eu.pickride.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class PdfDocumentRepository {

    public void save(PdfDocumentDataModel doc) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(doc);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<PdfDocumentDataModel> findById(String id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            PdfDocumentDataModel doc = em.find(PdfDocumentDataModel.class, id);
            return Optional.ofNullable(doc);
        } finally {
            em.close();
        }
    }

    public List<PdfDocumentDataModel> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("FROM PdfDocumentDataModel", PdfDocumentDataModel.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void update(PdfDocumentDataModel doc) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(doc);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(String id) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            PdfDocumentDataModel doc = em.find(PdfDocumentDataModel.class, id);
            if (doc != null) {
                em.remove(doc); // triggers soft-delete via @SQLDelete
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}

