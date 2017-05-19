package prv.zielony.akka.typed.behaviors.full

case class User(id: String, credentials: Credentials, contactData: ContactData, personalData: PersonalData)

case class PersonalData(name: String, surname: String, age: Int)

case class ContactData(phone: Option[String], email: Option[String], address: Option[String])

case class Credentials(name: String, password: String)