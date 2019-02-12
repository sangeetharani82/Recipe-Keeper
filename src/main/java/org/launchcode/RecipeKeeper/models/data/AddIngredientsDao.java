package org.launchcode.RecipeKeeper.models.data;

import org.launchcode.RecipeKeeper.models.AddIngredientsToRecipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AddIngredientsDao extends CrudRepository<AddIngredientsToRecipe, Integer> {
}
