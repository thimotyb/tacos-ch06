package tacos.data;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import tacos.Taco;

public interface TacoRepository
         extends PagingAndSortingRepository<Taco, Long> {

  Taco save(Taco taco);

  Optional<Taco> findById(Long id);

}
