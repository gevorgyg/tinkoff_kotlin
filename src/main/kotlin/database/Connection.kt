package database

import domain_entities.*
import java.sql.DriverManager
import java.sql.Types

data class Connection(val db_url: String) {

    // Если вдруг кто-то еще будет компилить этот код:
    // для использования use с AutoCloseable нужно подключить либу в build.gradle,
    // см https://youtrack.jetbrains.com/issue/KT-41507

    fun initializeTables() {
        val conn = DriverManager.getConnection(db_url)
        conn.use {
            for (contentList in TableContents.tableData) {
                var stat = it.createStatement()
                stat.execute(ScriptsManager.generateTableCreationScript(contentList.first()))
                stat = it.createStatement()
                val changedRowsNum = stat.executeUpdate(ScriptsManager.generateInsertScript(contentList))

                if (changedRowsNum != contentList.size) {
                    println("SOMETHING WENT WRONG!!!!!!!!!!")
                    break
                }
            }
            println("TABLES CREATED")
            val rs = conn.metaData.getTables(null, null, null, null)
            while (rs.next()) {
                println(rs.getString("TABLE_NAME"))
            }
        }
    }

    fun selectById(tableName: String, id: Int, condition: Char): List<Application> {

        val conn = DriverManager.getConnection(db_url)
        val retList = ArrayList<Application>()
        conn.use {
            val sql = ScriptsManager.selectByIdScriptTemplate.format(tableName, condition)
            val ps = it.prepareStatement(sql)
            ps.setInt(1, id)
            val rs = ps.executeQuery()
            val md = rs.metaData
            while (rs.next()) {
                val resMap = HashMap<String, Any?>()
                for (i in 1..md.columnCount) {
                    val type: Int = md.getColumnType(i)
                    if (type == Types.VARCHAR || type == Types.CHAR) {
                        resMap[md.getColumnName(i)] = rs.getString(i)
                    } else {
                        resMap[md.getColumnName(i)] = rs.getInt(i)
                    }
                }
                if (tableName == "IDE")
                    retList.add(IDEOf(resMap))
                else if (tableName == "MediaViewer")
                    retList.add(mediaViewerOf(resMap))
                else if (tableName == "TextEditor")
                    retList.add(textEditorOf(resMap))
            }
        }
        return retList
    }

//    fun join(joinType: Service.JoinType): List<IDETextEditorJoined>

    fun clean() {
        val conn = DriverManager.getConnection(db_url)

        conn.use {
            for (sql in ScriptsManager.dropTablesScripts) {
                it.prepareStatement(sql).execute()
            }
        }
        println("DATABASE CLEAN")
    }
}

