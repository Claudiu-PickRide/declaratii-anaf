package eu.pickride.db.repo;

import eu.pickride.db.model.MonthlyUserDeclarationDataModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class MonthlyUserDeclarationRepository {

    private final EntityManager entityManager;

    public MonthlyUserDeclarationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // --- Basic CRUD ---

    public void save(MonthlyUserDeclarationDataModel declaration) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(declaration);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void update(MonthlyUserDeclarationDataModel declaration) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(declaration);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public MonthlyUserDeclarationDataModel findById(String id) {
        return entityManager.find(MonthlyUserDeclarationDataModel.class, id);
    }

    // --- Custom methods ---

    /**
     * Get the first declaration by userId, year, and month.
     */
    public MonthlyUserDeclarationDataModel findFirstByUserIdAndYearAndMonth(String userId, int year, int month) {
        try {
            TypedQuery<MonthlyUserDeclarationDataModel> query = entityManager.createQuery(
                    "SELECT d FROM MonthlyUserDeclaration d " +
                            "WHERE d.userId = :userId AND d.year = :year AND d.month = :month " +
                            "ORDER BY d.createdAt ASC",
                    MonthlyUserDeclarationDataModel.class
            );
            query.setParameter("userId", userId);
            query.setParameter("year", year);
            query.setParameter("month", month);
            query.setMaxResults(1);

            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Set or update a declaration's file path based on declaration name.
     * Supported names: "d100", "d300", "d390"
     */
    public void setOrUpdateFilePath(String id, String declarationName, String filePath) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();

            MonthlyUserDeclarationDataModel declaration = entityManager.find(MonthlyUserDeclarationDataModel.class, id);
            if (declaration == null) {
                throw new IllegalArgumentException("Declaration with id " + id + " not found");
            }

            switch (declarationName.toLowerCase()) {
                case "d100" -> declaration.setD100FileLocation(filePath);
                case "d300" -> declaration.setD300FileLocation(filePath);
                case "d390" -> declaration.setD390FileLocation(filePath);
                default -> throw new IllegalArgumentException("Unknown declaration name: " + declarationName);
            }

            entityManager.merge(declaration);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}
