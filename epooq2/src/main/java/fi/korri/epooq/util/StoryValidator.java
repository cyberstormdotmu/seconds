package fi.korri.epooq.util;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fi.korri.epooq.model.Story;

public class StoryValidator implements Validator 
{

	@Override
	public boolean supports(Class<?> story) 
	{
		return Story.class.isAssignableFrom(story);
	}

	@Override
	public void validate(Object target, Errors errors) 
	{
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "story.titlerequired");
		
		
	}

}
