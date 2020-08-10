package Product.Offer;

import Product.Product;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import Product.Condition;
import Product.RepoProduct;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class OfferEndJob implements Job {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("pharmacy");
    EntityManager em = emf.createEntityManager();
    RepoProduct repoProduct = new RepoProduct(em);

    public void execute(JobExecutionContext context) throws JobExecutionException{
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int productId = dataMap.getInt("productId");

        em.getTransaction().begin();
        Product product = repoProduct.findProductById(productId);

        if(product == null){
            em.getTransaction().rollback();
            em.close();
            throw new NotInDatabaseException();
        }

        product.setCondition(Condition.NORMAL);
        em.getTransaction().commit();
        em.close();
    }
}
