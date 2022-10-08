package dev.efemoney.lexiko.anvil

import com.squareup.anvil.annotations.ExperimentalAnvilApi
import com.squareup.anvil.compiler.api.AnvilContext
import com.squareup.anvil.compiler.api.CodeGenerator
import com.squareup.anvil.compiler.api.GeneratedFile
import com.squareup.anvil.compiler.internal.reference.classAndInnerClassReferences
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile
import java.io.File

@OptIn(ExperimentalAnvilApi::class)
class GameEventHandlerGenerator : CodeGenerator {

  override fun isApplicable(context: AnvilContext) = true

  override fun generateCode(
    codeGenDir: File,
    module: ModuleDescriptor,
    projectFiles: Collection<KtFile>
  ): Collection<GeneratedFile> {

    projectFiles
      .classAndInnerClassReferences(module)
      .filter { it.isAnnotatedWith(FqName("dev.efemoney.lexiko.engine")) }

    return emptyList()
  }
}
