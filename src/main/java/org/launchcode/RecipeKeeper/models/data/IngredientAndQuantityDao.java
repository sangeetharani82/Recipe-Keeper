package org.launchcode.RecipeKeeper.models.data;

import org.launchcode.RecipeKeeper.models.IngredientAndQuantity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IngredientAndQuantityDao extends CrudRepository<IngredientAndQuantity, Integer> {
}
