package sortablerecord
import shapeless._
import ops.record._
import labelled._
import ops.hlist._
import tag._

object traverseHlistKeys extends Poly1 {
  implicit def caseRecord[K] =
    at[Symbol with Tagged[K]](s => s.name.asInstanceOf[K])
}

trait SortableRecord[T] {
  type Keys
  type KeysAsCoproduct
  type StringKeys
  val keysAsString: List[String]
  val keys: Keys
}

object SortableRecord {

  type Aux[T, O, O1, O2] = SortableRecord[T] {
    type Keys = O;
    type KeysAsCoproduct = O1;
    type StringKeys = O2;
    val keysAsString: List[String];
    val keys: O;
  }
  def apply[T](implicit
      inst: SortableRecord[T]
  ): Aux[T, inst.Keys, inst.KeysAsCoproduct, inst.StringKeys] = inst
  implicit def recordSortable[
      T,
      TRepr <: HList,
      KRepr <: HList,
      KCop <: Coproduct,
      KSRepr <: HList
  ](implicit
      lGen: LabelledGeneric.Aux[T, TRepr],
      keysGen: Keys.Aux[TRepr, KRepr],
      traversable: ToTraversable.Aux[KRepr, List, Symbol],
      mapper: Mapper.Aux[traverseHlistKeys.type, KRepr, KSRepr],
      toCoproduct: ToCoproduct.Aux[KSRepr, KCop]
  ): Aux[T, KRepr, KCop, KSRepr] = new SortableRecord[T] {
    type Keys = KRepr
    type KeysAsCoproduct = KCop
    type StringKeys = KSRepr
    val keys = keysGen.apply
    val keysAsString = keys.toList.map(s => s.name)
  }
}
