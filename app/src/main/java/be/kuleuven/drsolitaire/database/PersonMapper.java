package be.kuleuven.drsolitaire.database;

import android.util.Log;

import be.kuleuven.drsolitaire.classes.Person;

/**
 * Singleton to map Person entities to-and-from the database.
 * @author Koen Pelsmaekers
 */

public enum PersonMapper {
	UNIQUEMAPPER;

	private EntityMapper eMapper;

	/**
	 * Use only one entity mapper for all mappers
	 *
	 * @param entityMapper Applying the singleton design
	 */
	public void setEntityMapper(EntityMapper entityMapper) {
		this.eMapper = entityMapper;
	}

	public void getPersonByUsernameAndPassword(Person person) {
		String getPerson = "https://iiw.kuleuven.be/onderzoek/drSolitaire/getPerson.php";
		Log.d("DB", getPerson);
		eMapper.queryPersonLogin(person, getPerson);
	}


	/**
	 * Store a person in the database
	 *
	 * @param person The Person object that needs to be stored
	 */
	public void createPerson(Person person) {
		String createPerson = "https://iiw.kuleuven.be/onderzoek/drSolitaire/insertPerson.php";
		Log.d("DB",createPerson);
		eMapper.queryRegisterPerson(person, createPerson);
	}
}