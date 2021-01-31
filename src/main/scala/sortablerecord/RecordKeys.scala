package sortablerecord

import shapeless._
import labelled._

trait RecordKeys[R] {
  type Keys <: Coproduct

  def apply: Keys
}

object RecordKeys extends LowPriorityRecordKeysInstances {
  def apply[T](implicit inst: RecordKeys[T]): Aux[T, inst.Keys] = inst
  implicit def hnilRecordKeys: Aux[HNil, Unit :+: CNil] =
    new RecordKeys[HNil] {
      type Keys = Unit :+: CNil
      def apply: Keys = Inl(())
    }

  implicit def cconsRecordKeys1[K, V, T <: HList](implicit
      trs: RecordKeys[T],
      wit: Witness.Aux[K],
      lub: LUBConstraint[K, Symbol]
  ): Aux[FieldType[K, V] :: HNil, K :+: CNil] =
    new RecordKeys[FieldType[K, V] :: HNil] {
      type Keys = K :+: CNil

      def apply: Keys = Coproduct[K :+: CNil](wit.value)
    }
}

trait LowPriorityRecordKeysInstances {
  type Aux[R, K <: Coproduct] = RecordKeys[R] { type Keys = K }

  implicit def caseClassKeys[T, TRepr <: HList, MKRepr <: Coproduct](implicit
      lGen: LabelledGeneric.Aux[T, TRepr],
      rs: RecordKeys.Aux[TRepr, MKRepr]
  ): Aux[T, MKRepr] = new RecordKeys[T] {
    type Keys = MKRepr
    def apply: MKRepr = rs.apply
  }

  implicit def recordKeys[R <: HList](implicit
      rs: RecordKeys[R]
  ): rs.Keys = rs.apply

  implicit def cconsRecordKeys0[K, V, T <: HList](implicit
      trs: RecordKeys[T]
  ): Aux[FieldType[K, V] :: T, K :+: trs.Keys] =
    new RecordKeys[FieldType[K, V] :: T] {
      type Keys = K :+: trs.Keys

      def apply: K :+: trs.Keys = trs.apply.extendLeft[K]
    }
}
