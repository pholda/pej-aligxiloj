package util

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import pl.pej.malpompaaligxilo.form.field.{TableCheckboxRow, TableCheckboxCol, EnumOption}
import pl.pej.malpompaaligxilo.form.{Form, FormAction}
import pl.pej.malpompaaligxilo.util.Date

case class MongoInsertFormAction(dbName: String, collection: String) extends FormAction[Form] {
  override def run(form: Form): Unit = {
    val mongoClient = MongoClient()
    val parsedMapped = form.fields.filter(_.store).map{f =>
      f.name -> f.value(form)
    }.toMap.mapValues{
      case Some(EnumOption(name, _)) => Some(name)
        //todo fix warning
      case Some(s: Set[(TableCheckboxRow, TableCheckboxCol)]) => Some(s.map {
        case (r, c) => r.id -> c.id
      })
      case Some(d: Date) => Some(d.toString)
      case x => x
    }
    val mongoObj = MongoDBObject[String, Option[Any]](parsedMapped.toList)
    val db = mongoClient.getDB(dbName)
    db(collection).insert(mongoObj)
    mongoClient.close
  }
}