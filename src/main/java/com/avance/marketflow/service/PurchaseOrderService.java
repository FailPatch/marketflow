package com.avance.marketflow.service;

import com.avance.marketflow.dao.ArticleDao;
import com.avance.marketflow.dao.AuditDao;
import com.avance.marketflow.dao.EmployeeDao;
import com.avance.marketflow.dao.ProviderDao;
import com.avance.marketflow.dao.PurchaseOrderDao;
import com.avance.marketflow.model.Article;
import com.avance.marketflow.model.Employee;
import com.avance.marketflow.model.Provider;
import com.avance.marketflow.model.PurchaseOrder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {
    private final EmployeeDao employeeDao;
    private final ProviderDao providerDao;
    private final ArticleDao articleDao;
    private final PurchaseOrderDao purchaseOrderDao;
    private final AuditDao auditDao;

    public PurchaseOrderService(EmployeeDao employeeDao, ProviderDao providerDao, ArticleDao articleDao, PurchaseOrderDao purchaseOrderDao, AuditDao auditDao) {
        this.employeeDao = employeeDao;
        this.providerDao = providerDao;
        this.articleDao = articleDao;
        this.purchaseOrderDao = purchaseOrderDao;
        this.auditDao = auditDao;
    }

    public List<Employee> employees() { return employeeDao.findAll(); }
    public List<Provider> providers() { return providerDao.findAll(); }
    public List<Article> articles() { return articleDao.findAll(); }
    public List<Article> searchArticles(String query) {
        auditDao.add("Articulos", "BUSCAR", query == null ? "" : query);
        return articleDao.search(query);
    }
    public List<PurchaseOrder> orders() { return purchaseOrderDao.findAll(); }

    public Employee saveEmployee(String name, String email) {
        Employee employee = employeeDao.save(name, email);
        auditDao.add("Empleados", "REGISTRAR", email);
        return employee;
    }

    public Provider saveProvider(String name, String ruc) {
        Provider provider = providerDao.save(name, ruc);
        auditDao.add("Proveedores", "REGISTRAR", name);
        return provider;
    }

    public Optional<Provider> searchProvider(String query) {
        auditDao.add("Proveedores", "BUSCAR", query);
        return providerDao.search(query);
    }

    public Article saveArticle(String name, BigDecimal price) {
        Article article = articleDao.save(name, price);
        auditDao.add("Articulos", "GRABAR", name);
        return article;
    }

    public void updateArticle(long id, String name, BigDecimal price) {
        articleDao.update(id, name, price);
        auditDao.add("Articulos", "ACTUALIZAR", name);
    }

    public void deleteArticle(long id) {
        articleDao.delete(id);
        auditDao.add("Articulos", "ELIMINAR", "Articulo " + id);
    }

    public PurchaseOrder newOrder() {
        return purchaseOrderDao.create();
    }

    public void addArticle(PurchaseOrder order, long articleId) {
        articleDao.findById(articleId).ifPresent(order.getArticles()::add);
    }

    public void removeArticle(PurchaseOrder order, int index) {
        if (index >= 0 && index < order.getArticles().size()) {
            order.getArticles().remove(index);
        }
    }

    public void saveOrder(PurchaseOrder order, String providerName) {
        order.setProviderName(providerName);
        purchaseOrderDao.save(order);
        auditDao.add("Compras", "GRABAR_ORDEN", "Orden " + order.getId());
    }
}
