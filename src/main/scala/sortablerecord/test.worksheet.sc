import shapeless.ops.hlist.ToCoproduct
import shapeless.tag.Tagged
import shapeless.ops.tuple._
import shapeless._
import labelled._
import syntax.singleton._
import syntax.inject._
import sortablerecord.SortableRecord
case class IceCream(flavor: String, size: Int)
case class Gelato(gusto: String, dimensione: Int)

val iceCreamSRecordGen = SortableRecord[IceCream]

val sort = "size".narrow.inject[iceCreamSRecordGen.KeysAsCoproduct]
