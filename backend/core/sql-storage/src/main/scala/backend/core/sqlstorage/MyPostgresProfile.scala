package backend.core.sqlstorage

import com.github.tminglei.slickpg.*
import com.github.tminglei.slickpg.str.PgStringSupport
import slick.basic.Capability
import slick.jdbc.JdbcCapabilities

trait MyPostgresProfile extends ExPostgresProfile
  with PgArraySupport
  with PgNetSupport
  with PgSearchSupport
  with PgSprayJsonSupport
  with PgDate2Support
  with PgStringSupport
{
  def pgjson = "jsonb"
  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + JdbcCapabilities.insertOrUpdate

  override val api: MyAPI.type = MyAPI

  object MyAPI extends API
    with ArrayImplicits
    with SearchImplicits
    with SearchAssistants
    with SprayJsonImplicits
    with SprayJsonPlainImplicits
    with DateTimeImplicits
    with PgStringImplicits
}

object MyPostgresProfile extends MyPostgresProfile
