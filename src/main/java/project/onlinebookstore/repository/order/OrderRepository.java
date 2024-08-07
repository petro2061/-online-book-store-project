package project.onlinebookstore.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.onlinebookstore.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
