package prv.zielony.akka.typed.types

sealed trait Member {

  val name: String

  val roles: Seq[Role]
}

case class Role(name: String)

case class User(name: String, roles: Seq[Role], password: String, groups: Seq[Group])

case class Group(name: String, roles: Seq[Role], members: Seq[Member])

