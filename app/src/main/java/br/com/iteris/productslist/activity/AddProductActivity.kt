package br.com.iteris.productslist.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.RadioGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import br.com.iteris.productslist.Converter
import br.com.iteris.productslist.R
import br.com.iteris.productslist.databinding.ActivityAddProductBinding
import br.com.iteris.productslist.databinding.DialogRegisterImageBinding
import br.com.iteris.productslist.model.Product
import br.com.iteris.productslist.viewmodel.AddProductViewModel
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject

open class AddProductActivity : AppCompatActivity() {

    protected val viewModel : AddProductViewModel by inject()
    protected val binding : ActivityAddProductBinding by lazy { ActivityAddProductBinding.inflate(layoutInflater) }
    protected val bindingDialogRegisterImage : DialogRegisterImageBinding by lazy { DialogRegisterImageBinding.inflate(layoutInflater) }
    protected var uri : Uri? = null
    protected var byteArray : ByteArray? = null

    // Abrir galeria Contrato
    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent()) {
            it?.let {
                uri = it
                bindingDialogRegisterImage.registerImageIvProduct.setImageURI(it)
                byteArray = fromUriToByteArray(it)
        }
    }

    // Abrir Câmera - Contrato
    private val takeImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            bindingDialogRegisterImage.registerImageIvProduct.setImageBitmap(imageBitmap)
            byteArray = Converter.fromBitmapToByteArray(imageBitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            addProductBtnSaveProduct.setOnClickListener(btnSaveProductListener)

            // Input validation
            addProductEtProductName.addTextChangedListener(editTextListener(addProductEtProductName, addProductTilProductName))
            addProductEtProductDescription.addTextChangedListener(editTextListener(addProductEtProductDescription, addProductTilProductDescription))
            addProductEtProductPrice.addTextChangedListener(editTextListener(addProductEtProductPrice, addProductTilProductPrice))

            // Listener para abrir dialog de imagem
            addProductIvProduct.setOnClickListener(ivLoadImageListener)
        }

        // Caso todos editText sejam válidos
        viewModel.isValid.observe(this, {
            onSuccess(it)
        })

    }

    // Listener do botão de salvar o produto
    protected open val btnSaveProductListener = View.OnClickListener {
        with(binding) {
            addProductBtnSaveProduct.isClickable = false
            // Valida os inputs
            viewModel.validateInputs(
                addProductEtProductName,
                addProductTilProductName,
                addProductEtProductDescription,
                addProductTilProductDescription,
                addProductEtProductPrice,
                addProductTilProductPrice,
                byteArray
            )
        }
    }

    // Caso de sucesso validação dos campos
    private fun onSuccess(product : Product) {
        val intent = Intent().apply {
            putExtras(bundleOf(PRODUCT to product))
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    // Edit Text Listener
    private fun editTextListener(input : EditText, inputLayout : TextInputLayout) = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            viewModel.validateSingleInput(input, inputLayout)
            binding.addProductBtnSaveProduct.isClickable = true
        }

        override fun afterTextChanged(p0: Editable?) {}

    }

    // Listener para abrir dialog de cadastrar imagem
    private val ivLoadImageListener = View.OnClickListener {
        AlertDialog.Builder(this).apply {
            setCancelable(true)
            setView(bindingDialogRegisterImage.root)

            // Caso já tenha uma imagem
            loadImageIfNotNull()

            bindingDialogRegisterImage.registerImageBtnLoadImage.setOnClickListener {
                showImagePickerDialog()
            }

            setPositiveButton("Confirmar") { _, _ ->
                binding.addProductIvProduct.setImageBitmap(byteArray?.let { it1 ->
                    Converter.fromByteArrayToBitMap(it1)
                })
            }

            setNegativeButton("Cancelar") {_,_->}

        }.show()
    }

    // Mostra Dialog da Image Picker
    private fun showImagePickerDialog() {

        // Setando o dialog customizado criado para seleção de camera ou galeria
        val dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)  // Já tem um título no layout, entao tirei ele
            setCancelable(true) // Assim consegue clicar para fora e fechar o dialog
            setContentView(R.layout.dialog_image_picker) // seta o layout customizado para o dialog
        }

        // Pegando radioGroup do image_picker_dialog layout
        val radioGroupCameraGallery : RadioGroup = dialog.findViewById(R.id.radioGroupCameraGallery)

        // Click listener do radio group
        radioGroupCameraGallery.setOnCheckedChangeListener { _, i ->
            when(i) {
                // Abre galeria e fecha o dialog
                R.id.radio_gallery -> {
                    getImage.launch("image/")
                    dialog.dismiss()
                }
                // abre camera e fecha o dialog
                R.id.radio_camera -> {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takeImage.launch(takePicture)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    // Converte URI to ByteArray
    private fun fromUriToByteArray(uri : Uri) : ByteArray {
        return contentResolver.openInputStream(uri)!!.buffered().use { it.readBytes() }
    }

    // Método para editProductActivity usar para colocar imagem
    open fun loadImageIfNotNull() {
    }

    // Contrato da activity
    class ActivityContract : ActivityResultContract<String, Product>() {
        override fun createIntent(context: Context, input: String) =
            Intent(context, AddProductActivity::class.java)

        override fun parseResult(resultCode: Int, result: Intent?): Product? {
            if(resultCode != Activity.RESULT_OK) return null
            return result?.getSerializableExtra(PRODUCT) as Product?
        }
    }

    companion object {
        private const val PRODUCT = "PRODUCT"
    }

}